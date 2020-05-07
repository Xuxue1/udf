import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

@Description(name="FindStandardAddress"
        , value = "FindStandardAddress(string station_name, string address)===>" +
        "return address of station_name in the file: CityStationDict and append address in the end of the file",
        extended = "Example:\n"
        +"FindStandardAddress('杭州','安徽|340000|阜阳|341200|||测试阜阳站"
)
public class FindStandardAddress extends UDF{
    String stationName;
    String addressName;
    int stringsLength = 0;
    FindLCS maxLCS = new FindLCS();

    /*
    //append new address to the file:CityStationDict
    public static void writeAddress(String address) {
        try {
            FileWriter writer = new FileWriter("src/CityStationDict", true);
            writer.write(address);
            writer.close();
        }catch (IOException e){
            e.printStackTrace();

        }
    }
    */


    //get the address array
    public String FindAddress(String ...strings) {
        // 获取输入的参数
        if(strings.length == 0){
            try{
                throw new Exception("FUNCTION needs one parameter at least");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else if(strings.length > 2){
            try{
                throw new Exception("Function needs 2 parameter at most");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else if(strings.length == 1){
            stationName = strings[0];
        }
        else if(strings.length == 2) {
            stationName = strings[0];
            addressName = strings[1];
            //追加临时补充的新的映射地址
           /*
            if (addressName.split("\\|").length == 7) {
                writeAddress("\n" + addressName.replaceAll("\\|", "\t"));
            } else {
                try {
                    throw new Exception("Uncorrect address pattern.It should contaion 7 item after split with '|'");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            */
        }

        //返回最长子序列对应的标准地址
        String AddressList;
        HashSet<String> provinceName = new HashSet<>();//存放最后返回的结果
        HashSet<String> cityName = new HashSet<>();
        HashSet<String> areaName = new HashSet<>();

        if (stationName == null) return null;
        else {
            int maxLcs = 1;
            try{
                BufferedReader reader = new BufferedReader(new FileReader("src/CityStationDict"));
                String str;
                while((str = reader.readLine()) != null){
                    String [] stationArr = str.split("\t");
                    int tmpMaxLCS = maxLCS.findLCS(stationName, stationArr[6])[stationName.length()][stationArr[6].length()];
                    if(tmpMaxLCS > maxLcs) {//当发现更长的公共子序列 更新对应的地址
                        maxLcs = tmpMaxLCS;
                        provinceName.clear();
                        provinceName.add(stationArr[0]);
                        cityName.clear();
                        cityName.add(stationArr[2]);
                        areaName.clear();
                        areaName.add(stationArr[4]);
                    }
                    else if(tmpMaxLCS == maxLcs){//如果相等 判断是否需要更新
                        provinceName.add(stationArr[0]);
                        cityName.add(stationArr[2]);
                        areaName.add(stationArr[4]);
                        }
                    }

                } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String res = "";

        //保证回溯到根节点如果是同一个则返回改地点 否则返回null(多值是不确定的模糊数据)
        String province = provinceName.size()==1?provinceName.toString():"";
        String city = cityName.size()==1?cityName.toString():"";
        String area = areaName.size()==1?areaName.toString():"";
        res = province + "|" + city + "|" + area ;
        if(res.equals("||"))return null;
        else return res.replaceAll("\\[|\\]", "");

    }
    public String evaluate(String ...input){
       String res = FindAddress(input);
       return res;
    }
//    public static void main(String [] args){
//        FindStandardAddress res = new FindStandardAddress();
//        System.out.print(res.evaluate("杭"));
//    }
}
