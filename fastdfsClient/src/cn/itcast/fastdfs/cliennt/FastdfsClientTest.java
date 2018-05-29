package cn.itcast.fastdfs.cliennt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.FileInfo;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

/**
 * 
 * <p>Title: FastdfsClientTest</p>
 * <p>Description: fastdfs测试</p>
 * <p>Company: www.itcast.com</p> 
 * @author	传智.燕青
 * @date	2015-5-18上午9:35:30
 * @version 1.0
 */
public class FastdfsClientTest {
	
	//客户端配置文件
	public String conf_filename = "F:\\workspace_indigo\\fastdfsClient\\src\\cn\\itcast\\fastdfs\\cliennt\\fdfs_client.conf"; 
    //本地文件，要上传的文件
	public String local_filename = "F:\\develop\\upload\\linshiyaopinxinxi_20140423193847.xlsx"; 

    
    //将字节流写到磁盘生成文件
    private void saveFile(byte[] b, String path, String fileName) {
  		
    	File file = new File(path+fileName);
    	FileOutputStream fileOutputStream = null;
    	try {
			fileOutputStream= new FileOutputStream(file);
			
			fileOutputStream.write(b);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(fileOutputStream!=null){
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
  		
  	}
    
    //上传文件
    @Test 
    public void testUpload() { 
    	
    	for(int i=0;i<100;i++){

        try { 
            ClientGlobal.init(conf_filename); 

            TrackerClient tracker = new TrackerClient(); 
            TrackerServer trackerServer = tracker.getConnection(); 
            StorageServer storageServer = null; 

            StorageClient storageClient = new StorageClient(trackerServer, 
                    storageServer); 
            NameValuePair nvp [] = new NameValuePair[]{ 
                    new NameValuePair("item_id", "100010"), 
                    new NameValuePair("width", "80"),
                    new NameValuePair("height", "90")
            }; 
            String fileIds[] = storageClient.upload_file(local_filename, null, 
                    nvp); 

            System.out.println(fileIds.length); 
            System.out.println("组名：" + fileIds[0]); 
            System.out.println("路径: " + fileIds[1]); 

        } catch (FileNotFoundException e) { 
            e.printStackTrace(); 
        } catch (IOException e) { 
            e.printStackTrace(); 
        } catch (Exception e) {
			e.printStackTrace();
		} 
    	}
    } 

    //下载文件
    @Test 
    public void testDownload() { 

        try { 

            ClientGlobal.init(conf_filename); 

            TrackerClient tracker = new TrackerClient(); 
            TrackerServer trackerServer = tracker.getConnection(); 
            StorageServer storageServer = null; 

//            StorageClient storageClient = new StorageClient(trackerServer, 
//                    storageServer); 
            StorageClient1 storageClient = new StorageClient1(trackerServer, storageServer);
            byte[] b = storageClient.download_file("group1", 
                    "M00/00/00/wKhlBVVZvU6AV3MyAAE1Bar7bBg889.jpg"); 
            if(b !=null){
            	 System.out.println(b.length); 
                 saveFile(b, "F:\\develop\\upload\\temp\\", UUID.randomUUID().toString()+".jpg"); 
            }
           
        } catch (Exception e) { 
            e.printStackTrace(); 
        } 
    } 
  
    //获取文件信息
	@Test 
    public void testGetFileInfo(){ 
        try { 
            ClientGlobal.init(conf_filename); 

            TrackerClient tracker = new TrackerClient(); 
            TrackerServer trackerServer = tracker.getConnection(); 
            StorageServer storageServer = null; 

            StorageClient storageClient = new StorageClient(trackerServer, 
                    storageServer); 
            FileInfo fi = storageClient.get_file_info("group1", "M00/00/00/wKhlBVVZvU6AV3MyAAE1Bar7bBg889.jpg"); 
            System.out.println(fi.getSourceIpAddr()); 
            System.out.println(fi.getFileSize()); 
            System.out.println(fi.getCreateTimestamp()); 
            System.out.println(fi.getCrc32()); 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } 
    } 
	
	//获取文件自定义的mate信息(key、value)
    @Test 
    public void testGetFileMate(){ 
        try { 
            ClientGlobal.init(conf_filename); 

            TrackerClient tracker = new TrackerClient(); 
            TrackerServer trackerServer = tracker.getConnection(); 
            StorageServer storageServer = null; 

            StorageClient storageClient = new StorageClient(trackerServer, 
                    storageServer); 
            NameValuePair nvps [] = storageClient.get_metadata("group1", "M00/00/00/wKhlBVVZvU6AV3MyAAE1Bar7bBg889.jpg"); 
            if(nvps!=null){
            	for(NameValuePair nvp : nvps){ 
                    System.out.println(nvp.getName() + ":" + nvp.getValue()); 
                } 
            }
            
        } catch (Exception e) { 
            e.printStackTrace(); 
        } 
    } 
    //删除文件
    @Test 
    public void testDelete(){ 
        try { 
            ClientGlobal.init(conf_filename); 

            TrackerClient tracker = new TrackerClient(); 
            TrackerServer trackerServer = tracker.getConnection(); 
            StorageServer storageServer = null; 

            StorageClient storageClient = new StorageClient(trackerServer, 
                    storageServer); 
            int i = storageClient.delete_file("group1", "M00/00/00/wKhlBVVZvU6AV3MyAAE1Bar7bBg889.jpg"); 
            System.out.println( i==0 ? "删除成功" : "删除失败:"+i); 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } 
    } 
	

}
