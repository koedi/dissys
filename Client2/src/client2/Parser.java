/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client2;

/**
 *
 * @author retsi
 */
public class Parser {

    public Parser() {

    }

    public String nthWordFromMsg(String msg, int n) {

        int onword = 1;

        if (!msg.contains(" ")) {
            return msg;
        } else {
            for (int i = 0; i < msg.length(); i++) {
                if (msg.charAt(i) == ' ') {
                    onword++;
                }
                if (onword == n) {
                    for (int j = i + 1; j < msg.length(); j++) {
                        if (msg.charAt(j) == ' ') {
                            return msg.substring(i, j).trim();
                        }
                    }
                    return msg.substring(i).trim();
                }
            }
        }
        return "";
    }

}
