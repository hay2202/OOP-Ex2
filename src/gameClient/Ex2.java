package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.io.*;
import java.util.*;

public class Ex2 implements Runnable {
    private static MyFrame _win;
    private static Arena _ar;
    private static long playerID;
    private static int num_level;
    private static dw_graph_algorithms gAlgo;

    public static void main(String[] a) {
        login();
        Thread client = new Thread(new Ex2());
        client.start();
    }

    @Override
    public void run() {
        game_service game = Game_Server_Ex2.getServer(num_level); // you have [0,23] games
//			game.login(playerID);
        String g = game.getGraph();
      //  writeGraph(g);
        gAlgo = new DWGraph_Algo();
        gAlgo.load("loadGraph.txt");
        directed_weighted_graph gg = gAlgo.getGraph();
        init(game, gg);
        game.startGame();
        int ind = 0;
        long dt = 60;

        while (game.isRunning()) {
            moveAgents(game, gg);
            try {
                if (ind % 3 == 0) {
                    _win.repaint();
                }
               Thread.sleep(dt);
                ind++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String res = game.toString();

        System.out.println(res);
        System.exit(0);
    }

    /**
     * Moves each of the agents along the edge,
     * in case the agent is on a node the next destination (next edge) is chosen (randomly).
     *
     * @param game
     * @param gg
     * @param
     */
    private static void moveAgents(game_service game, directed_weighted_graph gg) {
        String lg = game.move();
        List<CL_Agent> log = Arena.getAgents(lg, gg);
        _ar.setAgents(log);
        String fs = game.getPokemons();
        Queue<CL_Pokemon> ffs = Arena.json2Pokemons(fs);
        _ar.setPokemons(ffs);
        for (int i = 0; i < log.size(); i++) {
            CL_Agent ag = log.get(i);
            int id = ag.getID();
            int dest = ag.getNextNode();
            double v = ag.getValue();
            if (dest == -1) {
                dest = nextNode(gg, ag);
                game.chooseNextEdge(ag.getID(), dest);
                System.out.println("Agent: " + id + ", val: " + v + "   turned to node: " + dest);
            }
        }

    }

    /**
     * a very simple random walk implementation!
     *
     * @param g
     * @param ag
     * @return
     */
    private static int nextNode(directed_weighted_graph g, CL_Agent ag) {
        if (ag.getPath() == null || ag.getPath().isEmpty()) {
                if (_ar.getPokemons().size() > 0) {
                    CL_Pokemon p = nearestPokemon(ag);
                    List<node_data> newPath = gAlgo.shortestPath(ag.getSrcNode(), p.get_edge().getSrc());
                    node_data des = g.getNode(p.get_edge().getDest());
                    newPath.add(des);
                    newPath.remove(0);
                    ag.setPath(newPath);
                }
        }
       if (!ag.getPath().isEmpty()) {
            node_data next = ag.getPath().get(0);
            ag.getPath().remove(0);
            return next.getKey();
        }

       return -20;
    }

    // initializes Arena, Frame and place first agents
    private void init(game_service game, directed_weighted_graph gg) {
        _ar = new Arena(gg, game);
        _win = new MyFrame("Ex2 - OOP: Pokemons! ");
        _win.setSize(1000, 700);
        _win.update(_ar);
        _win.show();

        String info = game.toString();
        JSONObject line;
        try {
            line = new JSONObject(info);
            JSONObject ttt = line.getJSONObject("GameServer");
            int numOfAgents = ttt.getInt("agents");
            System.out.println(info);
            System.out.println(game.getPokemons());
            int src_node;
            Queue<CL_Pokemon> listOfPokemons = Arena.json2Pokemons(game.getPokemons());
            for (int a = 0; a < numOfAgents; a++) {
                CL_Pokemon c = listOfPokemons.poll();
                src_node = c.get_edge().getDest();
                if (c.getType() < 0) {
                    src_node = c.get_edge().getSrc();
                }
                game.addAgent(src_node);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private static void login() {
        MyFrame frame = new MyFrame("log in ");
        frame.setBounds(200, 0, 500, 500);
        try {

            String id = JOptionPane.showInputDialog(frame, "Please insert ID", "Login", 3);
            String level = JOptionPane.showInputDialog(frame, "Please insert level number [0-23]", "Level", 3);

            playerID = Long.parseLong(id);
            num_level = Integer.parseInt(level);

            if (num_level > 23 || num_level < 0)
                throw new RuntimeException();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Invalid input.\nPlaying default game", "Error",
                    JOptionPane.ERROR_MESSAGE);
            num_level = 0;
        }
    }


    private static void writeGraph(String s) {
        try {
            FileWriter fw = new FileWriter("loadGraph.txt");
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(s);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static CL_Pokemon nearestPokemon(CL_Agent ag) {
        double minDist = Double.POSITIVE_INFINITY;
        CL_Pokemon target = null;
        for (int i = 0; i < _ar.getPokemons().size(); i++) {
            CL_Pokemon p = _ar.getPokemons().poll();
            if (p.getTag()) {
                double d = gAlgo.shortestPathDist(ag.getSrcNode(), p.get_edge().getDest());
                if (d < minDist) {
                    minDist = d;
                    target = p;
                }
            }
        }
        return target;
    }
}