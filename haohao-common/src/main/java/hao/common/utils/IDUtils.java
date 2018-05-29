package hao.common.utils;
import java.util.Random;

/**   
*    
* 项目名称：haohao-common   
* 类名称：IDUtils   
* 类描述：   各种Id生成策略
* 创建人：濠濠
* 创建时间：2018年5月1日 下午6:06:38   
* @version    1.0
*/
public class IDUtils {
	
	/**
	 * 商品Id生成
	 * @return
	 */
	public static long getItemId(){
		//取当前时间的长整形值包含毫秒
		long millis = System.currentTimeMillis();
		//加上2位随机数
		Random random = new Random();
		
		int i = random.nextInt(99);
		//如果不足2位前面补零
		String str = millis + String.format("%02d", i);
		
		return Long.parseLong(str);
		
	}
	
	/**
	 * 图片名生成
	 */
	public static String genImageName() {
		//取当前时间的长整形值包含毫秒
		long millis = System.currentTimeMillis();
		//long millis = System.nanoTime();
		//加上三位随机数
		Random random = new Random();
		int end3 = random.nextInt(999);
		//如果不足三位前面补0
		String str = millis + String.format("%03d", end3);
		
		return str;
	}
}
