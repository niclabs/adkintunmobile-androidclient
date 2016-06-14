package cl.niclabs.adkintunmobile.utils.information;


import android.content.Context;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class SystemSockets {

    static final private String TCP_PATH = "/proc/net/tcp";
    static final private String UDP_PATH = "/proc/net/udp";

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
        for (SystemSocket socket : SystemSockets.getTCPSockets()){
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
        for (SystemSocket socket : SystemSockets.getUDPSockets()){
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
                SystemSocket currentConnection = new SystemSocket(line);
                if ( currentConnection.isOutsideActiveConnection() ){
                    sockets.add(currentConnection);
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