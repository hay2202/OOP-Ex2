package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Range;
import gameClient.util.Range2D;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.List;

public class MyPanel extends JPanel{

        private Arena _ar;
        private gameClient.util.Range2Range _w2f;

        public MyPanel(){}

        public void update(Arena ar) {
            this._ar = ar;
            updateFrame();
        }

        private void updateFrame() {
            Range rx = new Range(20,this.getWidth()-20);
            Range ry = new Range(this.getHeight()-10,150);
            Range2D frame = new Range2D(rx,ry);
            directed_weighted_graph g = _ar.getGraph();
            _w2f = Arena.w2f(g,frame);
        }

        //paint on panel
        public void paint(Graphics g) {
            int w = this.getWidth();
            int h = this.getHeight();
            g.clearRect(0, 0, w, h);
            updateFrame();
            drawPokemons(g);
            drawGraph(g);
            drawAgents(g);
            drawTimer(g);
            drawScore(g);

        }

        //draw graph
        private void drawGraph(Graphics g) {
            directed_weighted_graph gg = _ar.getGraph();
            Iterator<node_data> iter = gg.getV().iterator();
            while(iter.hasNext()) {
                node_data n = iter.next();
                g.setColor(Color.blue);
                drawNode(n,5,g);
                Iterator<edge_data> itr = gg.getE(n.getKey()).iterator();
                while(itr.hasNext()) {
                    edge_data e = itr.next();
                    g.setColor(Color.gray);
                    drawEdge(e, g);
                }
            }
        }

        //draw pokemons
        private void drawPokemons(Graphics g) {
            java.util.ArrayList<CL_Pokemon> fs = _ar.getPokemons();
            if(fs!=null) {
                Iterator<CL_Pokemon> itr = fs.iterator();
                while(itr.hasNext()) {
                    CL_Pokemon f = itr.next();
                    geo_location c = f.getLocation();
                    int r=10;
                    g.setColor(Color.green);
                    if(f.getType()<0) {g.setColor(Color.orange);}
                    if(c!=null) {
                        geo_location fp = this._w2f.world2frame(c);
                        g.fillOval((int)fp.x()-r, (int)fp.y()-r, 2*r, 2*r);
                    }
                }
            }
        }

        //draw agents
        private void drawAgents(Graphics g) {
            java.util.List<CL_Agent> rs = _ar.getAgents();
            //	Iterator<OOP_Point3D> itr = rs.iterator();
            g.setColor(Color.red);
            int i=0;
            while(rs!=null && i<rs.size()) {
                geo_location c = rs.get(i).getLocation();
                int r=8;

                if(c!=null) {
                    geo_location fp = this._w2f.world2frame(c);
                    g.fillOval((int)fp.x()-r, (int)fp.y()-r, 2*r, 2*r);
                    g.drawString("agent: "+rs.get(i).getID(),(int)fp.x()-r, (int)fp.y()-r);
                }
                i++;
            }
        }

        //draw nodes
        private void drawNode(node_data n, int r, Graphics g) {
            geo_location pos = n.getLocation();
            geo_location fp = this._w2f.world2frame(pos);
            g.fillOval((int)fp.x()-r, (int)fp.y()-r, 2*r, 2*r);
            g.drawString(""+n.getKey(), (int)fp.x(), (int)fp.y()-4*r);
        }

        //draw edges
        private void drawEdge(edge_data e, Graphics g) {
            directed_weighted_graph gg = _ar.getGraph();
            geo_location s = gg.getNode(e.getSrc()).getLocation();
            geo_location d = gg.getNode(e.getDest()).getLocation();
            geo_location s0 = this._w2f.world2frame(s);
            geo_location d0 = this._w2f.world2frame(d);
            g.drawLine((int)s0.x(), (int)s0.y(), (int)d0.x(), (int)d0.y());
        }

        //draw timer
        private void drawTimer(Graphics g) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial",Font.BOLD,12));
            g.drawString("Time: "+_ar.getGame().timeToEnd()/1000,40 , 60);
        }

        //draw table of scores
        private void drawScore (Graphics g){
            List<CL_Agent> rs = _ar.getAgents();
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial",Font.BOLD,12));
            int y=60;
            int i=0;
            for (CL_Agent a : rs){
                g.drawString("agent "+ a.getID()+" :	 "+a.getValue(),900 , y+i);
                i+=20;
            }

        }
    }




