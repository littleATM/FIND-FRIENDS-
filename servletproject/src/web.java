import java.awt.List;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.*;
import javax.servlet.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class web extends HttpServlet{
	
	/**
	 * @throws IOException 
	 * 
	 */

	public void doPost(HttpServletRequest request, HttpServletResponse response)
	                                         throws ServletException,IOException{
		
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		StringBuffer sb=new StringBuffer();
		String str;
		PrintWriter out1=response.getWriter();
		while((str=request.getReader().readLine())!=null){
			sb.append(str);
			
		}
		try {
		
			Class.forName("com.mysql.jdbc.Driver").newInstance();		
			Connection conn=null;		
			conn=DriverManager.getConnection("jdbc:mysql://localhost/find_friends?" +
			
			"user=root&password=act123");		
			Statement stmt=conn.createStatement();	
			JSONObject requestData=new JSONObject(sb.toString());
			JSONObject responseData=new JSONObject();
			switch(requestData.getString("requestType")){
			    case "logIn":
			    	try {
						//JSONObject requestData=data.getJSONObject("requestData");

						ResultSet rs=stmt.executeQuery("SELECT * FROM find_friends.users WHERE user_name='"+requestData.getString("username")+"'");
						
						
			    		if(rs.next()&&(rs.getString("password")).equals(requestData.getString("password"))){
			    			responseData.accumulate("flag", "logInSuccess");
			    			//int temp=rs.getInt("count");
			    			//temp++;
			    			//stmt.executeUpdate("UPDATE find_friends.users SET count='"+temp+"' WHERE user_name='"+requestData.getString("username")+"'");
			    			improveJSONobject(requestData,responseData,stmt);
			    			out1.println(responseData.toString());	
			    			out1.flush();
			    		}
			    		else{
			    			responseData.accumulate("flag", "logInFail");
			    			out1.println(responseData.toString());			    			
			    			out1.flush();
			    		}
			    	} 
			    	catch (SQLException e) {
					// TODO Auto-generated catch block
			    			e.printStackTrace();
			    	}
			    	break;
			    	
			    case"signIn":
			    	try{
						//JSONObject requestData=data.getJSONObject("requestData");

			    		ResultSet rs=stmt.executeQuery("SELECT * FROM find_friends.users WHERE user_name='"+requestData.getString("username")+"'");
			    		if(rs.next()){
			    			responseData.accumulate("flag", "usernameExisting");			    			
			    			out1.println(responseData.toString());
			    			out1.flush();
			    		}
			    		else{
			    			stmt.executeUpdate("INSERT INTO find_friends.users (user_name, password) VALUES ('"
			    		    +requestData.getString("username")
			    		    +"', '"
			    		    +requestData.getString("password")
			    		    +"')");
			    			stmt.executeUpdate("CREATE TABLE find_friends."+requestData.getString("username")+"_friends_request (friends_request_name VARCHAR(45) NOT NULL,PRIMARY KEY (friends_request_name));");
			    			stmt.executeUpdate("CREATE TABLE find_friends."+requestData.getString("username")+"_friends (friends_name VARCHAR(45) NOT NULL,PRIMARY KEY (friends_name));");
			    			stmt.executeUpdate("UPDATE find_friends.users SET count='1' WHERE user_name='"+requestData.getString("username")+"'");
			                responseData.accumulate("flag", "signInSuccess");
			                //improveJSONobject(requestData,responseData,stmt);
			    	        out1.println(responseData.toString());			    			
			    	        out1.flush();

			    		}
			    			
			    	}
			    	catch (SQLException e) {
					// TODO Auto-generated catch block
			    			e.printStackTrace();
			    	}
			    	break;
			    case"findFriends":
			    	try{
			    		if(requestData.getString("findFriendsName").equals(requestData.getString("username"))){
			    			responseData.accumulate("flag", "youself");	    			
			    			out1.println(responseData.toString());
			    			out1.flush();
			    			break;
			    		}
			    		ResultSet rs=stmt.executeQuery("SELECT * FROM find_friends.users WHERE user_name='"+requestData.getString("findFriendsName")+"'");
			    		if(rs.next()){
			    			rs=stmt.executeQuery("SELECT * FROM find_friends."+requestData.getString("findFriendsName")+"_friends WHERE friends_name='"+requestData.getString("username")+"'");
				    		if(rs.next()){
				    			responseData.accumulate("flag", "friendsExisting");	    			
				    			out1.println(responseData.toString());
				    			out1.flush();
				    			break;
				    		}
				    		else{
				    			
			    			rs=stmt.executeQuery("SELECT * FROM find_friends."+requestData.getString("findFriendsName")+"_friends_request WHERE friends_request_name='"+requestData.getString("username")+"'");
			    			if(!rs.next()){
			    				stmt.executeUpdate("INSERT INTO find_friends."+requestData.getString("findFriendsName")+"_friends_request (friends_request_name) VALUES ('"+requestData.getString("username")+"')");
			    			}
			    			responseData.accumulate("flag", "findFriendsSuccess");	    			
			    			out1.println(responseData.toString());
			    			out1.flush();
			    			break;
			    			}
			    		}
			    		else{
			    			responseData.accumulate("flag", "findFriendsFail");
			    			
			    			out1.println(responseData.toString());
			    			out1.flush();
			    			break;
			    		}
			    	
			    
			    	}
			    	catch (SQLException e) {
					// TODO Auto-generated catch block
			    			e.printStackTrace();
			    	}
			    	break;
			    case"deleteFriends":
			    	try{
			    		stmt.execute("DELETE FROM find_friends."+requestData.getString("username")+"_friends WHERE friends_name='"+requestData.getString("name")+"'");
			    		stmt.execute("DELETE FROM find_friends."+requestData.getString("name")+"_friends WHERE friends_name='"+requestData.getString("username")+"'");
			    		improveJSONobject(requestData,responseData,stmt);
		    			responseData.accumulate("flag", "deleteSuccess");	    			
		    			out1.println(responseData.toString());
		    			out1.flush();

			    		
			    	}			    	
			    	catch (SQLException e) {
						// TODO Auto-generated catch block
		    			e.printStackTrace();
		    			}		    			
			    	break;
			    case"acceptRequest":
			    	try{
			    		stmt.execute("DELETE FROM find_friends."+requestData.getString("username")+"_friends_request WHERE friends_request_name='"+requestData.getString("name")+"'");
			    		stmt.execute("INSERT INTO find_friends."+requestData.getString("name")+"_friends (friends_name) VALUES ('"+requestData.getString("username")+"')");
			    		stmt.execute("INSERT INTO find_friends."+requestData.getString("username")+"_friends (friends_name) VALUES ('"+requestData.getString("name")+"')");
			    		//improveJSONobject(data,responseData,stmt);
			    		responseData.accumulate("flag", "acceptSuccess");	    			
		    			out1.println(responseData.toString());
		    			out1.flush();

			    	}
			    	catch (SQLException e) {
						// TODO Auto-generated catch block
		    			e.printStackTrace();
		    			}	
			    	break;
			    case"rejectRequest":
			    	try{
			    		stmt.execute("DELETE FROM find_friends."+requestData.getString("username")+"_friends_request WHERE friends_request_name='"+requestData.getString("name")+"'");			    	
		    			responseData.accumulate("flag", "rejectSuccess");	    			
		    			out1.println(responseData.toString());
		    			out1.flush();

			    	}
			    	catch (SQLException e) {
						// TODO Auto-generated catch block
		    			e.printStackTrace();
		    			}		    			
			    	break;
			    case"friendsIist":
			    	try {
					    ResultSet rsf=stmt.executeQuery("SELECT friends_name FROM find_friends."+requestData.getString("username")+
					    "_friends");
					    JSONArray jaf=new JSONArray();										    
					    while(rsf.next()){
					    	jaf.put(rsf.getString("friends_name"));
					    }					    
					    responseData.accumulate("flag","friendsIistSuccess");
					    responseData.accumulate("responseData",jaf);			    			
					    out1.println(responseData.toString());
		    			out1.flush();				    					    					 					    					    
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			        break;
			    case"friendsRequestIist":
			    	try {
					    ResultSet rsfq=stmt.executeQuery("SELECT friends_request_name FROM find_friends."+requestData.getString("username")+							 
					    "_friends_request");							    
					    JSONArray jafq=new JSONArray();							    
					    while(rsfq.next()){							    										
					    jafq.put(rsfq.getString("friends_request_name"));										    
					    }							    
					    responseData.accumulate("responseData", jafq);				    
					    responseData.accumulate("flag","friendsRequestIistSuccess");			    			
					    out1.println(responseData.toString());
		    			out1.flush();				    					    					 					    					    
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			        break;
			    case"specifiedFriendsLocation":
			    	try {

			    		updateLocation(requestData, stmt, conn);			    		
			    		ResultSet rs=stmt.executeQuery("SELECT * FROM find_friends.users WHERE user_name='"+requestData.getString("specifiedFriendsName")+"'");
						rs.next();
						responseData.accumulate("longitude", rs.getDouble("longitude"));
						responseData.accumulate("latitude", rs.getDouble("latitude"));	
						
						Timestamp d = new Timestamp(System.currentTimeMillis());
						responseData.accumulate("time", (d.getTime()-rs.getTimestamp("last_online").getTime())/1000);	
						if(rs.getDouble("longitude")==0&&rs.getDouble("latitude")==0)
						    responseData.accumulate("flag", "null");
						else
					    responseData.accumulate("flag", "specifiedFriendsLocationSuccess");  					    
					    out1.println(responseData.toString());
		    			out1.flush();
					    
					    
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    break;
			    case"allFriendsLocation":
			    	try {
			    		updateLocation( requestData, stmt, conn);
			    		
			    		ResultSet rs=stmt.executeQuery("SELECT friends_name FROM find_friends."+requestData.getString("username")+
							    "_friends");
			    		JSONArray ja=new JSONArray();
			    		ArrayList<String> list=new ArrayList<String>();
					    while(rs.next()){
					    	list.add(rs.getString("friends_name"));
					    }	
						if(!list.isEmpty()){
						    responseData.accumulate("flag", "allFriendsLocationSuccess");	
						    for(String tmd:list){
						    	rs=stmt.executeQuery("SELECT * FROM find_friends.users WHERE user_name='"+tmd+"'");
						    	rs.next();
					    		JSONObject jo=new JSONObject();
					    		if(rs.getDouble("longitude")==0&&rs.getDouble("latitude")==0)
					    			continue;
					    		jo.accumulate("name", tmd);
					    		jo.accumulate("longitude", rs.getDouble("longitude"));
					    		jo.accumulate("latitude", rs.getDouble("latitude"));
					    		Timestamp d = new Timestamp(System.currentTimeMillis());
					    		jo.accumulate("time", (d.getTime()-rs.getTimestamp("last_online").getTime())/1000);
					    		ja.put(jo);
						    	
						    }												
					    
						    responseData.accumulate("responseData", ja);
					    
						    out1.println(responseData.toString());
		    			
						    out1.flush();
		    			}
						else{
						    responseData.accumulate("flag", "allFriendsLocationNull");
						    out1.println(responseData.toString());
			    			out1.flush();
						}
					    
					    
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    break;
			    case"peopleNearbyLocation":
			    	try {
			    		updateLocation( requestData, stmt, conn);
			    		
			    		DatabaseMetaData md = conn.getMetaData();
			    		JSONArray ja=new JSONArray();
			    		for(int i=-1;i<=1;i++)
			    			for(int j=-1;j<=1;j++){
					    		ResultSet rs = md.getTables(null, null, (int)(requestData.getDouble("latitude")*23.33452378+i)+"_"+(int)(requestData.getDouble("longitude")*23.33452378+j), null);
								if (!rs.next()) {
									  //Table Exist
									continue;
									
								}	
					    		 rs=stmt.executeQuery("SELECT `name` FROM `find_friends`.`"
					    				+(int)(requestData.getDouble("latitude")*23.33452378+i)
					    				+"_"
					    				+(int)(requestData.getDouble("longitude")*23.33452378+j)+"`");
					    		ArrayList<String> list=new ArrayList<String>();
							    while(rs.next()){
							    	list.add(rs.getString("name"));
							    }	
								if(!list.isEmpty()){
								    for(String tmd:list){
								    	rs=stmt.executeQuery("SELECT * FROM find_friends.users WHERE user_name='"+tmd+"'");
								    	rs.next();
							    		JSONObject jo=new JSONObject();
							    		jo.accumulate("name", tmd);
							    		jo.accumulate("longitude", rs.getDouble("longitude"));
							    		jo.accumulate("latitude", rs.getDouble("latitude"));
							    		Timestamp d = new Timestamp(System.currentTimeMillis());
							    		jo.accumulate("time", (d.getTime()-rs.getTimestamp("last_online").getTime())/1000);
							    		ja.put(jo);
								    	
								    }																		    

				    			}
			    			}
			    		responseData.accumulate("responseData", ja);
					    responseData.accumulate("flag", "peopleNearbyLocationSuccess");	
					    out1.println(responseData.toString());
		    			
					    out1.flush();
					    
					    
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    break;
			    default:			    			
			    	responseData.accumulate("flag", "error");			    			
                    out1.println(responseData.toString());			    			
                    out1.flush();
	    			break;
			}
			/*PrintWriter out1=response.getWriter();

			out1.println(str);
			out1.flush();*/
;
		} catch (JSONException e) {                                    
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	public void improveJSONobject(JSONObject requestData,JSONObject responseData,Statement stmt) {
		
		try {
		    ResultSet rsf=stmt.executeQuery("SELECT friends_name FROM find_friends."+requestData.getString("username")+
		    "_friends");
		    JSONArray jaf=new JSONArray();
		    ArrayList<String> list=new ArrayList <String>();
		
		    
		    while(rsf.next()){
		    	list.add(rsf.getString("friends_name"));
		    }
		    for(String tmp:list){
				JSONObject jo=new JSONObject();
				ResultSet rs2=stmt.executeQuery("SELECT * FROM find_friends.users WHERE user_name='"+tmp+"'");
				rs2.next();
				jo.accumulate("longitude", rs2.getString("longitude"));
				jo.accumulate("latitude", rs2.getString("latitude"));
				jo.accumulate("name", tmp);
				jaf.put(jo);			
				}
		    
		    responseData.accumulate("friends", jaf);
		    
		    
		    
		    ResultSet rsfq=stmt.executeQuery("SELECT friends_request_name FROM find_friends."+requestData.getString("username")+
		    "_friends_request");
		    JSONArray jafq=new JSONArray();
		    while(rsfq.next()){
		    	
				jafq.put(rsfq.getString("friends_request_name"));			
		    }
		    responseData.accumulate("friendsRequest", jafq);
		    
		    
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	public void updateLocation(JSONObject requestData,Statement stmt,Connection conn) {
		
		try {
			ResultSet rs=stmt.executeQuery("SELECT * FROM find_friends.users WHERE user_name='"+requestData.getString("username")+"'");
			rs.next();
		
			if(rs.getDouble("latitude")!=0&&rs.getDouble("longitude")!=0)

				stmt.execute("DELETE FROM `find_friends`.`"+(int)(rs.getDouble("latitude")*23.33452378)+"_"+(int)(rs.getDouble("longitude")*23.33452378)+"` WHERE `name`='"+requestData.getString("username")+"'");	
			DatabaseMetaData md = conn.getMetaData();
			rs = md.getTables(null, null, (int)(requestData.getDouble("latitude")*23.33452378)+"_"+(int)(requestData.getDouble("longitude")*23.33452378), null);
			if (!rs.next()) {
				  //Table Exist
				stmt.executeUpdate("CREATE TABLE `find_friends`.`"+(int)(requestData.getDouble("latitude")*23.33452378)+"_"+(int)(requestData.getDouble("longitude")*23.33452378)+"` (`name` VARCHAR(45) NOT NULL,PRIMARY KEY (`name`));");
				
			}	
			
			stmt.executeUpdate("INSERT INTO `find_friends`.`"
			+(int)(requestData.getDouble("latitude")*23.33452378)
			+"_"
			+(int)(requestData.getDouble("longitude")*23.33452378)
			+"` (`name` ) VALUES ('"+requestData.getString("username")+"');");		
			
			rs=stmt.executeQuery("SELECT * FROM find_friends.users WHERE user_name='"+requestData.getString("username")+"'");
    		rs.next();
    		int temp=rs.getInt("count");
			temp++;
			stmt.executeUpdate("UPDATE find_friends.users SET count='"+temp+"' WHERE user_name='"+requestData.getString("username")+"'");
    		stmt.executeUpdate("UPDATE find_friends.users SET longitude='"+requestData.getDouble("longitude")+"' WHERE user_name='"+requestData.getString("username")+"'");
    		stmt.executeUpdate("UPDATE find_friends.users SET latitude='"+requestData.getDouble("latitude")+"' WHERE user_name='"+requestData.getString("username")+"'");	
			

		    
		    
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	

}
