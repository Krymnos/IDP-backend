package io.provenance.controllers.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import io.provenance.model.Context;
import io.provenance.model.Datapoint;
import io.provenance.model.InputDatapoint;
import io.provenance.model.Location;
import io.provenance.model.Node;

public class ControllerHelper {
	
	public static Datapoint queryDatapoint(Session session, PreparedStatement prepared, String id) {
		Row row = session.execute(prepared.bind(id)).one();
		if(row != null) {
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
			Datapoint dp = new Datapoint(dpId, dpContext);
			Map<String, String> inputDPs = row.getMap("inputDPs", String.class, String.class);
			for(String key : inputDPs.keySet())
				dp.setInputDatapoint(new InputDatapoint(queryDatapoint(session, prepared, key), inputDPs.get(key)));
			return dp;
		} else
			return null;
	}
	
	public static List<Node> queryTopology(Session session, String query) {
		Map<String, Node> topology = new HashMap<String, Node>();
		ResultSet rows = session.execute(query);
		for(Row row : rows) {
			String id = row.getString("id"), name = row.getString("name"), successor = row.getString("successor");
			if(topology.containsKey(id)) {
				Node node = topology.get(id);
				node.setId(id);
				node.setName(name);
				node.setSuccessor(successor);
				topology.put(id, node);
			}
			else {
				Node node = new Node(id, name, successor);
				topology.put(id, node);
			}
			if(!id.equals(successor)) {
				if(topology.containsKey(successor)) {
					Node node = topology.get(successor);
					node.setPredecessor(id);
					topology.put(successor, node);
				}
				else
					topology.put(successor, new Node(id));
			}
		}
		List<Node> nodes = new ArrayList<Node>();
		for( String key : topology.keySet())
			nodes.add(topology.get(key));
		return nodes;
	}
}
