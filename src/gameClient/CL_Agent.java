package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CL_Agent {
		private int _id;
		private geo_location _pos;
		private double _speed;
		private edge_data _curr_edge;
		private node_data _curr_node;
		private directed_weighted_graph _gg;
		private CL_Pokemon _curr_fruit;
		private List<node_data> path;
		private double _value;

		public CL_Agent(directed_weighted_graph g, int start_node) {
			_gg = g;
			setMoney(0);
			this._curr_node = _gg.getNode(start_node);
			_pos = _curr_node.getLocation();
			_id = -1;
			setSpeed(0);
			path = new ArrayList<>();
		}
		public void update(String json) {
			JSONObject line;
			try {
				line = new JSONObject(json);
				JSONObject ttt = line.getJSONObject("Agent");
				int id = ttt.getInt("id");
				if(id==this.getID() || this.getID() == -1) {
					if(this.getID() == -1) {_id = id;}
					double speed = ttt.getDouble("speed");
					String p = ttt.getString("pos");
					Point3D pp = new Point3D(p);
					int src = ttt.getInt("src");
					int dest = ttt.getInt("dest");
					double value = ttt.getDouble("value");
					this._pos = pp;
					this.setCurrNode(src);
					this.setSpeed(speed);
					this.setNextNode(dest);
					this.setMoney(value);
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		//@Override
		public int getSrcNode() {return this._curr_node.getKey();}

		public String toJSON() {
			int d = this.getNextNode();
			String ans = "{\"Agent\":{"
					+ "\"id\":"+this._id+","
					+ "\"value\":"+this._value+","
					+ "\"src\":"+this._curr_node.getKey()+","
					+ "\"dest\":"+d+","
					+ "\"speed\":"+this.getSpeed()+","
					+ "\"pos\":\""+_pos.toString()+"\""
					+ "}"
					+ "}";
			return ans;
		}
		private void setMoney(double v) {_value = v;}

		public boolean setNextNode(int dest) {
			boolean ans = false;
			int src = this._curr_node.getKey();
			this._curr_edge = _gg.getEdge(src, dest);
			if(_curr_edge!=null) {
				ans=true;
			}
			else {_curr_edge = null;}
			return ans;
		}

		public void setCurrNode(int src) {
			this._curr_node = _gg.getNode(src);
		}

		public boolean isMoving() {
			return this._curr_edge!=null;
		}

		public String toString() {
			return toJSON();
		}

		public String toString1() {
			String ans=""+this.getID()+","+_pos+", "+isMoving()+","+this.getValue();
			return ans;
		}

		public int getID() {
			return this._id;
		}

		public geo_location getLocation() {
			return _pos;
		}

		public double getValue() {
			return this._value;
		}

		public int getNextNode() {
			int ans = -2;
			if(this._curr_edge==null) {
				ans = -1;}
			else {
				ans = this._curr_edge.getDest();
			}
			return ans;
		}

		public double getSpeed() {
			return this._speed;
		}

		public void setSpeed(double v) {
			this._speed = v;
		}

		public CL_Pokemon get_curr_fruit() {
			return _curr_fruit;
		}

		public void set_curr_fruit(CL_Pokemon curr_fruit) {
			this._curr_fruit = curr_fruit;
		}

		public edge_data get_curr_edge() {
			return this._curr_edge;
		}
		public List<node_data> getPath (){
			return this.path;
		}
	public void setPath (List<node_data> p ){
		this.path=p;
	}

}