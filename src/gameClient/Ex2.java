package gameClient;

import Server.Game_Server_Ex2;
import api.*;

import javax.swing.*;
import java.io.*;
import java.util.*;

public class Ex2 implements Runnable{
    private static MyFrame _win;
    private static Arena _ar;
    private static long playerID;
    private static int  num_level;
    private static dw_graph_algorithms gAlgo;
    private static LinkedList<Integer>[] passed;

    public static void main(String[] a){
        num_level = -1;
        playerID = -1;
        if(a !=null ){
            int l = a.length;
            if(l>0)
                playerID = Integer.parseInt(a[0]);
            if(l>1)
                num_level = Integer.parseInt(a[1]);
        }
        Thread client = new Thread(new Ex2());
        client.start();


    }

    @Override
    public void run() {
        if(num_level == -1 && playerID == -1)
            login();
        game_service game = Game_Server_Ex2.getServer(num_level); // you have [0,23] games
        game.login(playerID);
        String g = game.getGraph();
        writeGraph(g);
        gAlgo = new DWGraph_Algo();
        gAlgo.load("loadGraph.txt");
        directed_weighted_graph gg =gAlgo.getGraph();
        init(game,gg);

        game.startGame();
        int ind=0;
        long dt=85;
        int mod=2;

        while(game.isRunning()) {
            moveAgents(game, gg);
            _ar.refresh();
            try {
                for (CL_Agent ag : _ar.getAgents()){
                    if (isStuck(ag.getID())){
                        dt=10;
                        mod=2;
                    }
                }
                if(ind%mod==0) {
                    game.move();
                    _win.repaint();
                }
                Thread.sleep(dt);
                ind++;
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            int count =0;
            for (int i=0; i<_ar.getAgents().size();i++){
                if(_ar.getAgents().get(i).getSpeed()>1)
                    count++;
            }
            if (count==_ar.getAgents().size()){
                dt=15;
                mod=8;
            }
            else
            {
                dt=85;
                mod=2;
            }
        }
        String res = game.toString();

        System.out.println(res);
        System.exit(0);
    }

    /**
     * Moves each of the agents along the edge,
     * in case the agent is on a node the next destination (next edge) is chosen by algorithm.
     * @param game
     * @param gg
     * @param
     */
        private static void moveAgents(game_service game, directed_weighted_graph gg) {
            List<CL_Agent> log = _ar.getAgents();
            synchronized (_ar.getAgents()) {
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
    }

    /**
     * algorithm to choose the next node of each agent
     * @param g
    * @param ag
     * @return node_id of the next destination.
     */
    private static int nextNode(directed_weighted_graph g, CL_Agent ag) {
        if (ag.getPath()== null || ag.getPath().isEmpty()){
            synchronized (_ar.getPokemons()){
            if(_ar.getPokemons().size()>0) {
                CL_Pokemon p = nearestPokemon(ag);
                List<node_data> newPath = gAlgo.shortestPath(ag.getSrcNode(), p.get_edge().getSrc());
                node_data des = g.getNode(p.get_edge().getDest());
                newPath.add(des);
                newPath.remove(0);
                ag.setPath(newPath);
            }}
        }
        node_data next = ag.getPath().get(0);
        ag.getPath().remove(0);
        passed[ag.getID()].add(next.getKey());
        return next.getKey();
    }

    // initializes Arena and Frame.
    private void init(game_service game, directed_weighted_graph gg) {
        _ar = new Arena(gg, game);
        _win = new MyFrame("Ex2 - OOP: Pokemons! Level: "+num_level);
        _win.setSize(1000, 700);
        _win.update(_ar);
        _win.show();
        passed = new LinkedList[_ar.getAgents().size()];
        for (int i = 0; i< passed.length; i++)
            passed[i]= new LinkedList<>();
    }

    // login to the game. get ID and LEVEL from the user.
    private static void login(){
        MyFrame frame = new MyFrame("log in ");
        frame.setBounds(200, 0, 500, 500);
        try {

            String id= JOptionPane.showInputDialog(frame, "Please insert ID","Login",3);
            String level = JOptionPane.showInputDialog(frame, "Please insert level number [0-23]","Level",3);

            playerID = Long.parseLong(id);
            num_level = Integer.parseInt(level);

            if (num_level > 23 || num_level < 0 )
                throw new RuntimeException();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Invalid input.\nPlaying default game", "Error",
                    JOptionPane.ERROR_MESSAGE);
            num_level = 0;
        }
    }

    //writing the JSON String into a file
    private static void writeGraph(String s){
        try {
            FileWriter fw = new FileWriter("loadGraph.txt");
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(s);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * this algorithm find the nearest pokemon with the given agent by using DWGraph_Algo function.
     * @param ag
     * @return the nearest pokemon.
     */
    private static CL_Pokemon nearestPokemon( CL_Agent ag){
        double minDist = Double.POSITIVE_INFINITY;
        CL_Pokemon target = null;
            for (int i = 0; i < _ar.getPokemons().size(); i++) {
                CL_Pokemon p = _ar.getPokemons().get(i);
                if (p.getTagged()!=1 || _ar.getGraph().getE(p.get_edge().getDest()) != null){
                    double d = gAlgo.shortestPathDist(ag.getSrcNode(), p.get_edge().getDest());
                    if (d < minDist) {
                        minDist = d;
                        target = p;
                    }
                }
            }
         target.setTagged(1);
        return target;
    }

    //this function check if there is agent stuck on edge if so changing dt
    public boolean isStuck( int key){
        if( passed[key].size()==6){
            LinkedList<Integer> temp = passed[key];
            if (temp.get(0)==temp.get(2)&& temp.get(0)==temp.get(4) && temp.get(1)==temp.get(3)&& temp.get(1)==temp.get(5)){
                return true;
            }
            passed[key]= new LinkedList<>();
        }
        return false;
    }
}