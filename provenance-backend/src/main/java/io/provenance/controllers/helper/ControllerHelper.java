package io.provenance.controllers.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import io.provenance.model.Context;
import io.provenance.model.Datapoint;
import io.provenance.model.InputDatapoint;
import io.provenance.model.Location;
import io.provenance.model.Node;
import io.provenance.model.NodeStat;

public class ControllerHelper {
	
	static final long NUM_100NS_INTERVALS_SINCE_UUID_EPOCH = 0x01b21dd213814000L;
	
	public static List<Node> queryTopology(Session session, String query) {
		List<Node> nodes = new ArrayList<Node>();
		ResultSet rows = session.execute(query);
		for(Row row : rows)
			nodes.add(new Node(row.getString("id"), row.getString("name"), row.getString("successor")));
		return nodes;
	}
	
	public static List<NodeStat> queryStats(Session session, String healthQuery, String rateQuery) {
		List<NodeStat> clusterStats = new ArrayList<NodeStat>();
		Set<String> nodeIDs = new HashSet<String>();
		Map<String, Long> heartbetas = new HashMap<String, Long>();
		Map<String, Map<String, Object>> rates = new HashMap<String, Map<String,Object>>();
		ResultSet healthResultSet = session.execute(healthQuery);
		ResultSet rateResultSet = session.execute(rateQuery);
		for(Row row : healthResultSet.all()) {
			String id = row.getString("id");
			nodeIDs.add(id);
			heartbetas.put(id, row.getTimestamp("time").getTime());
		}
		for(Row row : rateResultSet.all()) {
			String id = row.getString("id");
			nodeIDs.add(id);
			Map<String, Object> nodeStats = new HashMap<String, Object>();
			nodeStats.put("srate", row.getDouble("srate"));
			nodeStats.put("rrate", row.getDouble("rrate"));
			nodeStats.put("time", row.getTimestamp("time").getTime());
			rates.put(id, nodeStats);
		}
		for(String nodeID : nodeIDs) {
			long rateTime = 0, heartbeatTime = 0;
			String id = null;
			double sendRate = 0.0, receiveRate = 0.0;
			if(heartbetas.containsKey(nodeID)) {
				id = nodeID;
				heartbeatTime = heartbetas.get(nodeID);
			}
			if(rates.containsKey(nodeID)) {
				Map<String, Object> nodeStats = rates.get(nodeID);
				id = nodeID;
				sendRate = (double) nodeStats.get("srate");
				receiveRate = (double) nodeStats.get("rrate");
				rateTime = (long) nodeStats.get("time");
			}
			long recentTime = heartbeatTime > rateTime ? heartbeatTime : rateTime;
			NodeStat nodeStats = new NodeStat(id);
			nodeStats.setSendRate(sendRate);
			nodeStats.setReceiveRate(receiveRate);
			nodeStats.setHealth(getHealthStatus(recentTime));
			clusterStats.add(nodeStats);
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
	
	public static Datapoint queryDatapoint(Session session, String query, String id) {
		String finalQuery = String.format(query, id);
		Row row = session.execute(finalQuery).one();
		if(row != null) {
			Datapoint dp = getDaatapoint(row);
			Map<String, String> inputDPs = row.getMap("inputDPs", String.class, String.class);
			for(String key : inputDPs.keySet())
				dp.setInputDatapoint(new InputDatapoint(queryDatapoint(session, query, key), inputDPs.get(key)));
			return dp;
		} else
			return null;
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
		if((currentTime-time) <= 300000)
			return "Green";
		else if((currentTime-time) <= 600000)
			return "Yellow";
		else
			return "Red";
	}
	
	public static long getTimeFromUUID(UUID uuid) {
		return (uuid.timestamp() - NUM_100NS_INTERVALS_SINCE_UUID_EPOCH) / 10000;
	}
}