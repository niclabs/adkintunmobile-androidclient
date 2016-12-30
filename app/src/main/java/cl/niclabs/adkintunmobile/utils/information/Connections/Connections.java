package cl.niclabs.adkintunmobile.utils.information.Connections;


import android.content.Context;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Connections {

    static final private String TCP_PATH = "/proc/net/tcp";
    static final private String UDP_PATH = "/proc/net/udp";

    public enum Type{
        TCP,
        UDP,
        TCP6,
        UDP6
    }

    /**
     * Retorna un número constante de la clase Connections.java identificando el tipo de sockets rescatados
     * @param path
     * @return Type.{TCP, UDP, TCP6, UDP6}
     */
    public static Type getSocketTypeByPath(String path){
        String vFile = path.split("/")[path.split("/").length - 1];
        switch (vFile){
            case "tcp":
                return Type.TCP;
            case "udp":
                return Type.UDP;
            case "tcp6":
                return Type.TCP6;
            case "udp6":
                return Type.UDP6;
        }
        return null;
    }

    /**
     * Retorna un ArrayList con obtejos SystemSocket de las conexiones TCP activas
     * @return
     */
    public static ArrayList<SystemSocket> getTCPSockets() {
        ArrayList<SystemSocket> sockets = new ArrayList<SystemSocket>();
        parseSocketTable(TCP_PATH, sockets);
        return sockets;
    }

    /**
     * Retorna un ArrayList con obtejos SystemSocket de las conexiones UDP activas
     * @return
     */
    public static ArrayList<SystemSocket> getUDPSockets() {
        ArrayList<SystemSocket> sockets = new ArrayList<SystemSocket>();
        parseSocketTable(UDP_PATH, sockets);
        return sockets;
    }


    /**
     * Retorna un diccionario con nombres de aplicaciones y numero de conexiones activas en TCP
     */
    public static HashMap<String, Integer> getTCPActiveAppsConnections(Context context){
        HashMap<String, Integer> totals = new HashMap<String, Integer>();
        for (SystemSocket socket : Connections.getTCPSockets()){
            if (!totals.containsKey(socket.getAppName(context)))
                totals.put(socket.getAppName(context), 0);
            totals.put(socket.getAppName(context), totals.get(socket.getAppName(context)) + 1);
        }
        return totals;
    }

    /**
     * Retorna un diccionario con nombres de aplicaciones y numero de conexiones activas en UDP
     */
    public static HashMap<String, Integer> getUDPActiveAppsConnections(Context context){
        HashMap<String, Integer> totals = new HashMap<String, Integer>();
        for (SystemSocket socket : Connections.getUDPSockets()){
            if (!totals.containsKey(socket.getAppName(context)))
                totals.put(socket.getAppName(context), 0);
            totals.put(socket.getAppName(context), totals.get(socket.getAppName(context)) + 1);
        }
        return totals;
    }

    /**
     *
     * @param path el path al archivo especifico del directorio /proc/net/ que se desea parsear
     * @param sockets ArrayList con objetos SystemSocket con información de las conexiones activas
     */
    private static void parseSocketTable(String path, ArrayList<SystemSocket> sockets) {
        String line;
        try {
            BufferedReader socketInfoReader = new BufferedReader(new FileReader(path));
            socketInfoReader.readLine(); // Skip header

            while (( line = socketInfoReader.readLine() ) != null) {
                try {
                    SystemSocket currentConnection = new SystemSocket(line, Connections.getSocketTypeByPath(path));
                    if (currentConnection.isOutsideActiveConnection())
                        sockets.add(currentConnection);

                } catch (SystemSocketException e) {
                    e.printStackTrace();
                }
            }

            socketInfoReader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}