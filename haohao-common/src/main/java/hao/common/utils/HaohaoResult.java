/**
 * 
 */
package hao.common.utils;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**   
*    
* 项目名称：haohao-common   
* 类名称：HaohaoResult   
* 类描述：   封装商品操作回显数据
* 创建人：濠濠
* 创建时间：2018年5月1日 下午4:20:55   
* @version    1.0
*/
public class HaohaoResult implements Serializable {
	
	//定义jackson对象
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	//响应业务状态
	private Integer status;
	
	//响应消息
	private String msg;
	
	//响应数据
	private Object data;
	
	public static HaohaoResult build(Integer status, String msg, Object data){
		return new HaohaoResult(status, msg, data);
	}
	
	public HaohaoResult(Integer status, String msg, Object data) {
		super();
		this.status = status;
		this.msg = msg;
		this.data = data;
	}
	
	public static HaohaoResult build(Integer status, String msg){
		return new HaohaoResult(status, msg);
	}

	public HaohaoResult(Integer status, String msg) {
		super();
		this.status = status;
		this.msg = msg;
	}
	
	public static HaohaoResult ok(Object data){
		return new HaohaoResult(data);
	}
	
	
	public HaohaoResult(Object data) {
		super();
		this.data = data;
		this.msg = "OK";
		this.status = 200;
	}
	
	public static HaohaoResult ok(){
		return new HaohaoResult(null);
	}

	public HaohaoResult() {
		super();
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	/**
	 * 将结果Json转为HaohaoResult对象
	 * @param jsonData json数据
	 * @param clazz HaohaoResult中的object类型
	 * @return
	 */
	public static HaohaoResult formatToPojo(String jsonData, Class<?> clazz){
		try {
            if (clazz == null) {
                return MAPPER.readValue(jsonData, HaohaoResult.class);
            }
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            JsonNode data = jsonNode.get("data");
            Object obj = null;
            if (clazz != null) {
                if (data.isObject()) {
                    obj = MAPPER.readValue(data.traverse(), clazz);
                } else if (data.isTextual()) {
                    obj = MAPPER.readValue(data.asText(), clazz);
                }
            }
            return build(jsonNode.get("status").intValue(), jsonNode.get("msg").asText(), obj);
        } catch (Exception e) {
            return null;
        }
	}
	
	/**
	 * 	不带object参数的json数据转为HaohaoResult
	 * @param json
	 * @return
	 */
	public static HaohaoResult format(String json){
		try {
			return MAPPER.readValue(json, HaohaoResult.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 *  将HaohaoResult转化为集合根据传入的Class类型决定集合封装的类型
	 * @param jsonData jsonData json数据
	 * @param clazz  clazz 集合中的类型
	 * @return
	 */
	public static HaohaoResult formatToList(String jsonData, Class<?> clazz){
	     try {
	            JsonNode jsonNode = MAPPER.readTree(jsonData);
	            JsonNode data = jsonNode.get("data");
	            Object obj = null;
	            if (data.isArray() && data.size() > 0) {
	                obj = MAPPER.readValue(data.traverse(),
	                        MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
	            }
	            return build(jsonNode.get("status").intValue(), jsonNode.get("msg").asText(), obj);
	        } catch (Exception e) {
	            return null;
	        }
	}
	
	
	
	
	
}
