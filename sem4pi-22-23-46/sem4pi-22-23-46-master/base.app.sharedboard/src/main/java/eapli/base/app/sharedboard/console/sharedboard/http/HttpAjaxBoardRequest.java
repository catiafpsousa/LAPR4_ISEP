package eapli.base.app.sharedboard.console.sharedboard.http;

import eapli.base.app.sharedboard.console.sharedboard.client.TcpClient;

import java.io.*;
import java.net.Socket;


/**
 *
 * @author ANDRE MOREIRA (asc@isep.ipp.pt)
 */
public class HttpAjaxBoardRequest extends Thread {
	String baseFolder;
	Socket sock;
	DataInputStream inS;
	DataOutputStream outS;
	TcpClient client;

	Long boardID;

	public HttpAjaxBoardRequest(Socket s, String f, TcpClient client,Long boardID) {
		baseFolder = f;
		sock = s;
		this.client=client;
		this.boardID=boardID;
	}

	public void run() {
		try {
			outS = new DataOutputStream(sock.getOutputStream());
			inS = new DataInputStream(sock.getInputStream());
		} catch (IOException ex) {
			System.out.println("Thread error on data streams creation");
		}
		try {
			HTTPmessage request = new HTTPmessage(inS);
			HTTPmessage response = new HTTPmessage();
			if (request.getMethod().equals("GET")) {
				if (request.getURI().equals("/board")) {
					response.setContentFromString(HttpsServerAjaxBoard.getBoardStandingInHTML(client, boardID), "text/html");
					response.setResponseStatus("200 Ok");
				} else {
					String fullname = baseFolder + "/";
					if (request.getURI().equals("/")) fullname = fullname + "index.html";
					else fullname = fullname + request.getURI();
					if (response.setContentFromFile(fullname)) {
						response.setResponseStatus("200 Ok");
					} else {
						response.setContentFromString(
								"<html><body><h1>404 File not found</h1></body></html>",
								"text/html");
						response.setResponseStatus("404 Not Found");
					}
				}
				response.send(outS);
			} else { // NOT GET
				if (request.getMethod().equals("PUT")){
						/*
						&& request.getURI().startsWith("/board/")) {
					HttpsServerAjaxBoard.castVote(request.getURI().substring(7));
					response.setResponseStatus("200 Ok");

					 */

				} else {
					response.setContentFromString(
							"<html><body><h1>ERROR: 405 Method Not Allowed</h1></body></html>",
							"text/html");
					response.setResponseStatus("405 Method Not Allowed");
				}
				response.send(outS);
			}
		} catch (IOException ex) {
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		try {
			sock.close();
		} catch (IOException ex) {
			System.out.println("CLOSE IOException");
		}
	}
}

