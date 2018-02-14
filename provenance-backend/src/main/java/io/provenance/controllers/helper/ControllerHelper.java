package io.provenance.controllers.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import io.provenance.config.Config;
import io.provenance.exception.ConfigParseException;
import io.provenance.model.Context;
import io.provenance.model.Datapoint;
import io.provenance.model.InputDatapoint;
import io.provenance.model.Location;
import io.provenance.model.Node;
import io.provenance.model.NodeStat;
import io.provenance.model.PipelineDatapoint;
import io.provenance.model.ProvenanceResultSet;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.util.TablesNamesFinder;

public class ControllerHelper {
	
	public static List<Node> queryTopology(Session session, String query) {
		List<Node> nodes = new ArrayList<Node>();
		ResultSet rows = session.execute(query);
		for(Row row : rows)
			nodes.add(new Node(row.getString("id"), row.getString("name"), row.getString("successor")));
		return nodes;
	}
	
	public static List<NodeStat> queryStats(Session session, String healthQuery, String rateQuery) {
		List<NodeStat> clusterStats = new ArrayList<NodeStat>();
		Map<String, Map<String,Object>> nodeStats = new HashMap<String, Map<String,Object>>();
		Map<String, Long> channels = new HashMap<String, Long>();
		ResultSet healthResultSet = session.execute(healthQuery);
		ResultSet rateResultSet = session.execute(rateQuery);
		for(Row row : healthResultSet.all()) {
			String id = row.getString("id");
			Map<String, Object> stats = new HashMap<String, Object>();
			Map<String, Date> channelsStatus = row.getMap("channels", String.class, Date.class);
			for(String channelStatus : channelsStatus.keySet())
				channels.put(channelStatus, channelsStatus.get(channelStatus).getTime());
			stats.put("pldaemon", row.getTimestamp("pldaemon").getTime());
			stats.put("time", row.getTimestamp("time").getTime());
			nodeStats.put(id, stats);
		}
		for(Row row : rateResultSet.all()) {
			String id = row.getString("id");
			Map<String, Object> stats = null;
			if(nodeStats.containsKey(id))
				stats = nodeStats.get(id);
			else
				stats = new HashMap<String, Object>();
			if(getHealthStatus(row.getTimestamp("time").getTime()).equals("Green") ) {
				stats.put("srate", row.getDouble("srate"));
				stats.put("rrate", row.getDouble("rrate"));
			} else {
				stats.put("srate", 0.0);
				stats.put("rrate", 0.0);
			}
			nodeStats.put(id, stats);
		}
		for(String nodeID : nodeStats.keySet()) {
			Map<String, Object> stats = nodeStats.get(nodeID);
			NodeStat nodeStat = new NodeStat(nodeID);
			nodeStat.setSendRate((double)stats.get("srate"));
			nodeStat.setReceiveRate((double)stats.get("rrate"));
			nodeStat.setProvenanceDaemonHealth(getHealthStatus((long)stats.get("time")));
			nodeStat.setPipelineDaemonHealth(getHealthStatus((long)stats.get("pldaemon")));
			String channelID = null;
			long nodeLatestStatusTime = 0;
			long channelLatestStatusTime = 0;
			for(String channel : channels.keySet()) {
				if(channel.startsWith(nodeID)) {
					channelLatestStatusTime = ((long)channels.get(channel));
					channelID = channel;
				}
				if(channel.contains(nodeID)) {
					if(((long)channels.get(channel)) > nodeLatestStatusTime)
						nodeLatestStatusTime = ((long)channels.get(channel));
				}
			}
			if(channelID != null) {
				String endpoints[] = channelID.split(":");
				for(String channel : channels.keySet()) {
					if(channel.startsWith(endpoints[1]) && channel.endsWith(endpoints[0])) {
						if(((long)channels.get(channel)) > channelLatestStatusTime)
							channelLatestStatusTime = ((long)channels.get(channel));
					}
				}
				nodeStat.setOutgoingChannelHealth(getHealthStatus(channelLatestStatusTime));
			}
			nodeStat.setNodeHealth(getHealthStatus(nodeLatestStatusTime > (long)stats.get("time") ? nodeLatestStatusTime : (long)stats.get("time")));
			clusterStats.add(nodeStat);
		}
		return clusterStats;
	}

	public static List<Datapoint> queryDatapoints(Session session, String query) {
		List<Datapoint> datapoints = new ArrayList<Datapoint>();
		ResultSet result = session.execute(query);
		for(Row row : result.all())
			datapoints.add(getDaatapoint(row));
		return datapoints;
	}
	
	public static Datapoint queryDatapointRecursive(Session session, String query, String id) {
		String finalQuery = String.format(query, id);
		Row row = session.execute(finalQuery).one();
		if(row != null) {
			Datapoint dp = getDaatapoint(row);
			Map<String, String> inputDPs = row.getMap("inputDPs", String.class, String.class);
			for(String key : inputDPs.keySet())
				dp.setInputDatapoint(new InputDatapoint(queryDatapointRecursive(session, query, key), inputDPs.get(key)));
			return dp;
		} else
			return null;
	}
	
	public static List<Datapoint> queryDatapointLinear(Session session, String query, String id) {
		Queue<String> queue = new LinkedList<String>();
		List<Datapoint> datapoints = new ArrayList<Datapoint>();
		queue.add(id);
		while(!queue.isEmpty()) {
			String finalQuery = String.format(query, queue.poll());
			Row row = session.execute(finalQuery).one();
			if(row != null) {
				Datapoint dp = getDaatapoint(row);
				Map<String, String> inputDPs = row.getMap("inputDPs", String.class, String.class);
				for(String key : inputDPs.keySet()) {
					dp.setInputDatapoint(new InputDatapoint(new Datapoint(key), inputDPs.get(key)));
					queue.add(key);
				}
				datapoints.add(dp);
			}
		}
		return datapoints;
	}
	
	public static List<PipelineDatapoint> queryPipeLineDatapointLinear(Session session, String query, String id) {
		Queue<String> queue = new LinkedList<String>();
		List<Datapoint> datapoints = new ArrayList<Datapoint>();
		List<PipelineDatapoint> pipelineDatapoints = new ArrayList<PipelineDatapoint>();
		queue.add(id);
		while(!queue.isEmpty()) {
			String finalQuery = String.format(query, queue.poll());
			Row row = session.execute(finalQuery).one();
			if(row != null) {
				Datapoint dp = getDaatapoint(row);
				Map<String, String> inputDPs = row.getMap("inputDPs", String.class, String.class);
				for(String key : inputDPs.keySet()) {
					dp.setInputDatapoint(new InputDatapoint(new Datapoint(key), inputDPs.get(key)));
					queue.add(key);
				}
				datapoints.add(dp);
			}
		}
		for(Datapoint dp : datapoints) {
			PipelineDatapoint pDatapoint = new PipelineDatapoint(dp.getId(), dp.getContext());
			boolean flag = false;
			for(Datapoint pdp : datapoints) {
				for(InputDatapoint idp : pdp.getInputDatapoints()) {
					if(dp.getId().equals(idp.getDp().getId())) {
						pDatapoint.setSuccessor(pdp.getContext().getHostId());
						flag = true;
						break;
					}
				}
				if(flag == true)
					break;
			}
			pipelineDatapoints.add(pDatapoint);
		}
		return pipelineDatapoints;
	}
	
	public static Datapoint getDaatapoint(Row row) {
		String dpId = row.getString("id");
		Context dpContext = new Context();
		if(!row.isNull("location")) {
			Location loc = new Location(row.getString("location"));
			if(!row.isNull("latitude") && !row.isNull("longitude"))
				loc.setLotLong(row.getDouble("latitude"), row.getDouble("longitude"));
			dpContext.setLoc(loc);
		}
		if(!row.isNull("line"))
			dpContext.setLineNo(row.getLong("line"));
		if(!row.isNull("app"))
			dpContext.setAppName(row.getString("app"));
		if(!row.isNull("class"))
			dpContext.setClassName(row.getString("class"));
		if(!row.isNull("ctime"))
			dpContext.setTimestamp(row.getTimestamp("ctime"));
		if(!row.isNull("stime"))
			dpContext.setSendTime(row.getTimestamp("stime"));
		if(!row.isNull("rtime"))
			dpContext.setReceiveTime(row.getTimestamp("rtime"));
		if(!row.isNull("meterId"))
			dpContext.setMeterId(row.getString("meterId"));
		if(!row.isNull("metricId"))
			dpContext.setMetricId(row.getString("metricId"));
		dpContext.setHostId(dpId.substring(dpId.length() - 6));
		return new Datapoint(dpId, dpContext);
	}
	
	public static String getHealthStatus(long time) {
		long currentTime = System.currentTimeMillis();
		if((currentTime-time) <= 10000)
			return "Green";
		else if((currentTime-time) <= 20000)
			return "Yellow";
		else
			return "Red";
	}
	
	public static ProvenanceResultSet queryData(Session session, String query) {
		try {
			Statement stmt = CCJSqlParserUtil.parse(query);
			if(stmt instanceof Select) {
				Select select = (Select)stmt;
				String tempTableName = new TablesNamesFinder().getTableList(select).get(0);
				String finalQuery = query.replaceAll(tempTableName, String.format("%s.%s", Config.getKEYSPACE(), Config.getTABLE()));
				if(finalQuery.contains(" id")) {
					if(finalQuery.contains("where") && finalQuery.indexOf("where") < finalQuery.indexOf(" id")) {
						String subPart = finalQuery.substring(finalQuery.lastIndexOf("where"));
						String id =subPart.substring(subPart.indexOf("'")+1, subPart.lastIndexOf("'"));
						finalQuery = finalQuery.replaceAll(id, "%s");
						System.out.println(finalQuery);
						Datapoint dp = ControllerHelper.queryDatapointRecursive(session, finalQuery, id);
						List<Datapoint> dps = new ArrayList<Datapoint>();
						dps.add(dp);
						return new ProvenanceResultSet(false, dps);
					}
				}
				List<Datapoint> datapoints = queryDatapoints(session, finalQuery);
				return new ProvenanceResultSet(false, datapoints);
			} else 
				return new ProvenanceResultSet(false, "PQL only supports select type queries.");
		} catch (Exception e) {
			return new ProvenanceResultSet(true, e.getMessage());
		}
	}
}