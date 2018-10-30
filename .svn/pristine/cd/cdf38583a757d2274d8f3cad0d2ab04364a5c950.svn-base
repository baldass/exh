package com.txts.util;


import java.io.BufferedReader;  
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;  
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;  
import java.net.HttpURLConnection;  
import java.net.URL;
import java.util.List;
import java.util.Map;  
import java.util.Set;  
  
  
/** 
 * 提供通过HTTP协议获取内容的方法 <br/> 
 * 所有提供方法中的params参数在内部不会进行自动的url encode，如果提交参数需要进行url encode，请调用方自行处理 
 * @Description: HTTP请求代理工具 
 * @author lushuifa 
 * @date 2016年11月18日 上午11:21:05 
 */  
public class HttpRequestUtil {  

      
    /** 
     * 支持的Http method 
     * 
     */  
    private static enum HttpMethod {  
        POST,DELETE,GET,PUT,HEAD;  
    };  
      
    @SuppressWarnings({ "unchecked", "rawtypes" })  
    private static String invokeUrl(String url, Map params, Map<String,String> headers, int connectTimeout, int readTimeout, String encoding, HttpMethod method){  
        //构造请求参数字符串  
        StringBuilder paramsStr = null;  
        if(params != null){  
            paramsStr = new StringBuilder();  
            Set<Map.Entry> entries = params.entrySet();  
            for(Map.Entry entry:entries){  
                String value = (entry.getValue()!=null)?(String.valueOf(entry.getValue())):"";  
                paramsStr.append(entry.getKey() + "=" + value + "&");  
            }  
            //只有POST方法才能通过OutputStream(即form的形式)提交参数  
            if(method != HttpMethod.POST){  
                url += "?"+paramsStr.toString();  
            }  
        }  
          
        URL uUrl = null;  
        HttpURLConnection conn = null;  
        BufferedWriter out = null;  
        BufferedReader in = null;  
        try {  
            //创建和初始化连接  
            uUrl = new URL(url);  
            conn = (HttpURLConnection) uUrl.openConnection();  
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");  
            conn.setRequestMethod(method.toString());  
            conn.setDoOutput(true);  
            conn.setDoInput(true);  
            //设置连接超时时间  
            conn.setConnectTimeout(connectTimeout);  
            //设置读取超时时间  
            conn.setReadTimeout(readTimeout);  
            //指定请求header参数  
            if(headers != null && headers.size() > 0){  
                Set<String> headerSet = headers.keySet();  
                for(String key:headerSet){  
                    conn.setRequestProperty(key, headers.get(key));  
                }  
            }  
  
            if(paramsStr != null && method == HttpMethod.POST){  
                //发送请求参数  
                out = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(),encoding));  
                out.write(paramsStr.toString());  
                out.flush();  
            }  
              
            //接收返回结果  
            StringBuilder result = new StringBuilder();  
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(),encoding));  
            if(in != null){  
                String line = "";  
                while ((line = in.readLine()) != null) {  
                    result.append(line);  
                }  
            }  
            return result.toString();  
        } catch (Exception e) {  
            System.out.println("调用接口["+url+"]失败！请求URL："+url+"，参数："+params+",异常："+e.getMessage()); 
            e.printStackTrace();
            //处理错误流，提高http连接被重用的几率  
            try {  
                byte[] buf = new byte[100];  
                InputStream es = conn.getErrorStream();  
                if(es != null){  
                    while (es.read(buf) > 0) {;}  
                    es.close();  
                }  
            } catch (Exception e1) {  
                e1.printStackTrace();  
            }  
        } finally {  
            try {  
                if (out!=null) {  
                    out.close();  
                }  
            }catch (Exception e) {  
                e.printStackTrace();  
            }  
            try {  
                if (in !=null) {  
                    in.close();  
                }  
            }catch (Exception e) {  
                e.printStackTrace();  
            }  
            //关闭连接  
            if (conn != null){  
                conn.disconnect();  
            } 
        }  
        return null;  
    }  
      
    /** 
     * POST方法提交Http请求，语义为“增加” <br/> 
     * 注意：Http方法中只有POST方法才能使用body来提交内容 
     * @param url 资源路径（如果url中已经包含参数，则params应该为null） 
     * @param params 参数 
     * @param connectTimeout 连接超时时间（单位为ms） 
     * @param readTimeout 读取超时时间（单位为ms） 
     * @param charset 字符集（一般该为“utf-8”） 
     * @return 
     */  
    public static String post(String url, Map params, int connectTimeout, int readTimeout, String charset){  
        return invokeUrl(url,params,null,connectTimeout,readTimeout,charset,HttpMethod.POST);  
    }  
    public static byte[] postForFile(String url, Map params, int connectTimeout, int readTimeout, String charset){  
        return invokeUrlForFile(url,params,null,connectTimeout,readTimeout,charset,HttpMethod.POST);  
    }
    public static byte[] getForFile(String url, Map params, int connectTimeout, int readTimeout, String charset){  
        return invokeUrlForFile(url,params,null,connectTimeout,readTimeout,charset,HttpMethod.GET);  
    }
    /** 
     * POST方法提交Http请求，语义为“增加” <br/> 
     * 注意：Http方法中只有POST方法才能使用body来提交内容 
     * @param url 资源路径（如果url中已经包含参数，则params应该为null） 
     * @param params 参数 
     * @param headers 请求头参数 
     * @param connectTimeout 连接超时时间（单位为ms） 
     * @param readTimeout 读取超时时间（单位为ms） 
     * @param charset 字符集（一般该为“utf-8”） 
     * @return 
     */  
    public static String post(String url, Map params, Map<String,String> headers, int connectTimeout, int readTimeout, String charset){  
        return invokeUrl(url,params,headers,connectTimeout,readTimeout,charset,HttpMethod.POST);  
    }  
      
    /** 
     * GET方法提交Http请求，语义为“查询” 
     * @param url 资源路径（如果url中已经包含参数，则params应该为null） 
     * @param params 参数 
     * @param connectTimeout 连接超时时间（单位为ms） 
     * @param readTimeout 读取超时时间（单位为ms） 
     * @param charset 字符集（一般该为“utf-8”） 
     * @return 
     */  
    public static String get(String url, Map params, int connectTimeout, int readTimeout, String charset){  
        return invokeUrl(url,params,null,connectTimeout,readTimeout,charset,HttpMethod.GET);  
    }  
      
    /** 
     * GET方法提交Http请求，语义为“查询” 
     * @param url 资源路径（如果url中已经包含参数，则params应该为null） 
     * @param params 参数 
     * @param headers 请求头参数 
     * @param connectTimeout 连接超时时间（单位为ms） 
     * @param readTimeout 读取超时时间（单位为ms） 
     * @param charset 字符集（一般该为“utf-8”） 
     * @return 
     */  
    public static String get(String url, Map params, Map<String,String> headers,int connectTimeout, int readTimeout, String charset){  
        return invokeUrl(url,params,headers,connectTimeout,readTimeout,charset,HttpMethod.GET);  
    }  
      
    /** 
     * PUT方法提交Http请求，语义为“更改” <br/> 
     * 注意：PUT方法也是使用url提交参数内容而非body，所以参数最大长度收到服务器端实现的限制，Resin大概是8K 
     * @param url 资源路径（如果url中已经包含参数，则params应该为null） 
     * @param params 参数 
     * @param connectTimeout 连接超时时间（单位为ms） 
     * @param readTimeout 读取超时时间（单位为ms） 
     * @param charset 字符集（一般该为“utf-8”） 
     * @return 
     */  
    public static String put(String url, Map params, int connectTimeout, int readTimeout, String charset){  
        return invokeUrl(url,params,null,connectTimeout,readTimeout,charset,HttpMethod.PUT);  
    }  
      
    /** 
     * PUT方法提交Http请求，语义为“更改” <br/> 
     * 注意：PUT方法也是使用url提交参数内容而非body，所以参数最大长度收到服务器端实现的限制，Resin大概是8K 
     * @param url 资源路径（如果url中已经包含参数，则params应该为null） 
     * @param params 参数 
     * @param headers 请求头参数 
     * @param connectTimeout 连接超时时间（单位为ms） 
     * @param readTimeout 读取超时时间（单位为ms） 
     * @param charset 字符集（一般该为“utf-8”） 
     * @return 
     */  
    public static String put(String url, Map params, Map<String,String> headers,int connectTimeout, int readTimeout, String charset){  
        return invokeUrl(url,params,headers,connectTimeout,readTimeout,charset,HttpMethod.PUT);  
    }     
      
    /** 
     * DELETE方法提交Http请求，语义为“删除” 
     * @param url 资源路径（如果url中已经包含参数，则params应该为null） 
     * @param params 参数 
     * @param connectTimeout 连接超时时间（单位为ms） 
     * @param readTimeout 读取超时时间（单位为ms） 
     * @param charset 字符集（一般该为“utf-8”） 
     * @return 
     */  
    public static String delete(String url, Map params, int connectTimeout, int readTimeout, String charset){  
        return invokeUrl(url,params,null,connectTimeout,readTimeout,charset,HttpMethod.DELETE);  
    }  
      
    /** 
     * DELETE方法提交Http请求，语义为“删除” 
     * @param url 资源路径（如果url中已经包含参数，则params应该为null） 
     * @param params 参数 
     * @param headers 请求头参数 
     * @param connectTimeout 连接超时时间（单位为ms） 
     * @param readTimeout 读取超时时间（单位为ms） 
     * @param charset 字符集（一般该为“utf-8”） 
     * @return 
     */  
    public static String delete(String url, Map params, Map<String,String> headers, int connectTimeout, int readTimeout, String charset){  
        return invokeUrl(url,params,headers,connectTimeout,readTimeout,charset,HttpMethod.DELETE);  
    }  
      
    /** 
     * HEAD方法提交Http请求，语义同GET方法  <br/> 
     * 跟GET方法不同的是，用该方法请求，服务端不返回message body只返回头信息，能节省带宽 
     * @param url 资源路径（如果url中已经包含参数，则params应该为null） 
     * @param params 参数 
     * @param connectTimeout 连接超时时间（单位为ms） 
     * @param readTimeout 读取超时时间（单位为ms） 
     * @param charset 字符集（一般该为“utf-8”） 
     * @return 
     */  
    public static String head(String url, Map params, int connectTimeout, int readTimeout, String charset){  
        return invokeUrl(url,params,null,connectTimeout,readTimeout,charset,HttpMethod.HEAD);  
    }  
      
    /** 
     * HEAD方法提交Http请求，语义同GET方法  <br/> 
     * 跟GET方法不同的是，用该方法请求，服务端不返回message body只返回头信息，能节省带宽 
     * @param url 资源路径（如果url中已经包含参数，则params应该为null） 
     * @param params 参数 
     * @param headers 请求头参数 
     * @param connectTimeout 连接超时时间（单位为ms） 
     * @param readTimeout 读取超时时间（单位为ms） 
     * @param charset 字符集（一般该为“utf-8”） 
     * @return 
     */  
    public static String head(String url, Map params, Map<String,String> headers, int connectTimeout, int readTimeout, String charset){  
        return invokeUrl(url,params,headers,connectTimeout,readTimeout,charset,HttpMethod.HEAD);  
    }  
    
    @SuppressWarnings({ "unchecked", "rawtypes" })  
    private static  byte[] invokeUrlForFile(String url, Map params, Map<String,String> headers, int connectTimeout, int readTimeout, String encoding, HttpMethod method){  
        //构造请求参数字符串  
        StringBuilder paramsStr = null;  
        if(params != null){  
            paramsStr = new StringBuilder();  
            Set<Map.Entry> entries = params.entrySet();  
            for(Map.Entry entry:entries){  
                String value = (entry.getValue()!=null)?(String.valueOf(entry.getValue())):"";  
                paramsStr.append(entry.getKey() + "=" + value + "&");  
            }  
            //只有POST方法才能通过OutputStream(即form的形式)提交参数  
            if(method != HttpMethod.POST){  
                url += "?"+paramsStr.toString();  
            }  
        }  
          
        URL uUrl = null;  
        HttpURLConnection conn = null;  
        BufferedWriter out = null;  
        try {  
            //创建和初始化连接  
            uUrl = new URL(url);  
            conn = (HttpURLConnection) uUrl.openConnection();  
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");  
            conn.setRequestMethod(method.toString());  
            conn.setDoOutput(true);  
            conn.setDoInput(true);  
            //设置连接超时时间  
            conn.setConnectTimeout(connectTimeout);  
            //设置读取超时时间  
            conn.setReadTimeout(readTimeout);  
            //指定请求header参数  
            if(headers != null && headers.size() > 0){  
                Set<String> headerSet = headers.keySet();  
                for(String key:headerSet){  
                    conn.setRequestProperty(key, headers.get(key));  
                }  
            }  
            
            if(paramsStr != null && method == HttpMethod.POST){  
                //发送请求参数  
                out = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(),encoding));  
                out.write(paramsStr.toString());  
                out.flush();  
            }  
              
           //接收返回结果  
           /* StringBuilder result = new StringBuilder();  
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(),encoding));  
            if(in != null){  
                String line = "";  
                while ((line = in.readLine()) != null) {  
                    result.append(line);  
                }  
            }  */
            InputStream is = conn.getInputStream();
            byte[] b =new byte[is.available()];
            is.read(b);
            return b; 
        } catch (Exception e) {  
            System.out.println("调用接口["+url+"]失败！请求URL："+url+"，参数："+params+",异常："+e.getMessage()); 
            e.printStackTrace();
            //处理错误流，提高http连接被重用的几率  
            try {  
                byte[] buf = new byte[100];  
                InputStream es = conn.getErrorStream();  
                if(es != null){  
                    while (es.read(buf) > 0) {;}  
                    es.close();  
                }  
            } catch (Exception e1) {  
                e1.printStackTrace();  
            }  
        } finally {  
            try {  
                if (out!=null) {  
                    out.close();  
                }  
            }catch (Exception e) {  
                e.printStackTrace();  
            }  
            
            //关闭连接  
            if (conn != null){  
                conn.disconnect();  
            }     
        }  
        return null;  
    }  
    /**
     * 带文件的http请求
     * @param fileName
     * @param urlPath
     * @param Files
     * @author lfy
     * @time 2018年5月18日 - 下午4:57:54
     */
    public static void uploadFile(String urlPath,List<Map> Files,Map params,String method ) {  
        try {  
  
        	StringBuilder paramsStr = null;  
            if(params != null){  
                paramsStr = new StringBuilder();  
                Set<Map.Entry> entries = params.entrySet();  
                for(Map.Entry entry:entries){  
                    String value = (entry.getValue()!=null)?(String.valueOf(entry.getValue())):"";  
                    paramsStr.append(entry.getKey() + "=" + value + "&");  
                }  
                //只有POST方法才能通过OutputStream(即form的形式)提交参数  
                //if("get".equals(method)){  
                    urlPath += "?"+paramsStr.toString();  
                //}  
            }  
            // 换行符  
            final String newLine = "\r\n";  
            final String boundaryPrefix = "--";  
            // 定义数据分隔线  
            String BOUNDARY = "========7d4a6d158c9";  
            // 服务器的域名  
            URL url = new URL(urlPath);  
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
            // 设置为POST情  
            conn.setRequestMethod("POST");  
            // 发送POST请求必须设置如下两行  
            conn.setDoOutput(true);  
            conn.setDoInput(true);  
            conn.setUseCaches(false);  
            // 设置请求头参数  
            conn.setRequestProperty("connection", "Keep-Alive");  
            conn.setRequestProperty("Charsert", "UTF-8");  
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);  
            OutputStream out = new DataOutputStream(conn.getOutputStream());  
            
            for (Map map : Files) {
				String fileName = map.get("fileName").toString();//名字
				byte[] b = (byte[]) map.get("bytes");
				
	            // 上传文件  
	            StringBuilder sb = new StringBuilder();  
	            sb.append(boundaryPrefix);  
	            sb.append(BOUNDARY);  
	            sb.append(newLine);  
	            // 文件参数,photo参数名可以随意修改  
	            sb.append("Content-Disposition: form-data;name=\""+fileName+"\";filename=\"" + fileName  
	                    + "\"" + newLine);  
	            sb.append("Content-Type:application/octet-stream");  
	            // 参数头设置完以后需要两个换行，然后才是参数内容  
	            sb.append(newLine);  
	            sb.append(newLine);  
	  
	            // 将参数头的数据写入到输出流中  
	            out.write(sb.toString().getBytes());  
	  
	            out.write(b);  
	            // 最后添加换行  
	            out.write(newLine.getBytes());   
	           
	            // 定义最后数据分隔线，即--加上BOUNDARY再加上--。  
	            byte[] end_data = (newLine + boundaryPrefix + BOUNDARY + boundaryPrefix + newLine)  
	                    .getBytes();  
	            // 写上结尾标识  
	            out.write(end_data);  
            }
            out.flush();  
            out.close();  
            // 定义BufferedReader输入流来读取URL的响应  
            BufferedReader reader = new BufferedReader(new InputStreamReader(  
                    conn.getInputStream()));  
            String line = null;  
            while ((line = reader.readLine()) != null) {  
                System.out.println(line);  
            }  
  
        } catch (Exception e) {  
            System.out.println("发送POST请求出现异常！" + e);  
            e.printStackTrace();  
        }  
    }  
    
    
}  