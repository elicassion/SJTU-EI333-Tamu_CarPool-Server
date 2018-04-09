package connnect_try;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

public class MatchQuery{
        public User user;
        private int poolType;
        private List<Point> points;
        private String startName;
        private String endName;
        private Point startPoint;
        private Point endPoint;
        public Date time;
        public int maxPsg;
        private int curPsg;
        private int queryNumber;
        private Set<ConfirmUser> have_confirmed_user;
        
        private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        MatchQuery(JSONObject jMatchQuery, int Query_Number){
            try {
                points = new ArrayList<Point>();
                String nid = jMatchQuery.getJSONObject("user").getString("id");
                int nuserType = jMatchQuery.getJSONObject("user").getInt("user_type");
                poolType = jMatchQuery.getInt("pool_type");
                user = new User(nid, nuserType);
                JSONArray jPoints = jMatchQuery.getJSONArray("points");
                for (int i = 0; i < jPoints.length(); ++i) {
                    JSONObject jPoint = (JSONObject) jPoints.opt(i);
                    points.add(new Point(jPoint.getDouble("lat"), jPoint.getDouble("lon")));
                }
                String timeString = jMatchQuery.getString("time");
                startName = jMatchQuery.getString("start_name");
                endName = jMatchQuery.getString("end_name");
                startPoint = new Point(jMatchQuery.getJSONObject("start_point").getDouble("lat"),
                                        jMatchQuery.getJSONObject("start_point").getDouble("lon"));
                endPoint = new Point(jMatchQuery.getJSONObject("end_point").getDouble("lat"),
                                        jMatchQuery.getJSONObject("end_point").getDouble("lon"));
                time = sdf.parse(timeString);
                //TODO: 
                curPsg = 0;
                maxPsg = 1;
                queryNumber = Query_Number;
                have_confirmed_user = new HashSet<ConfirmUser>();

            }catch(Exception ex){
                throw new RuntimeException(ex);
            }
        }

        public User getUser(){
            return user;
        }
        public int getPoolType(){
            return poolType;
        }

        public List<Point> getPoints() {
            return points;
        }

        public Point getEndPoint() {
            return endPoint;
        }

        public Point getStartPoint() {
            return startPoint;
        }

        public Date getTime() {
            return time;
        }

        public String getEndName() {
            return endName;
        }

        public String getStartName() {
            return startName;
        }

        public int getMaxPsg(){
            return maxPsg;
        }

        public int getCurPsg(){
            return curPsg;
        }

        public int getQueryNumber(){
            return queryNumber;
        }
        
        public Set<ConfirmUser> getHaveConfirmed_User()
        {
        	return have_confirmed_user;
        }
        
        public void addConfirmedUser(ConfirmUser cuser)
        {
        	have_confirmed_user.add(cuser);
        }
        public void incCurPsg()
        {
        	++curPsg;
        }
        public String getTimeString()
        {
        	return sdf.format(time);
        }
    }