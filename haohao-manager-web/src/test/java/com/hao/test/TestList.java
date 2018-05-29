/**
 * 
 */
package com.hao.test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 75659
 *
 */
public class TestList {
	
	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();
		list.add("aaa");
		list.add("bbb");
		list.add("ccc");
		int i =0;
		for (String string : list) {
			string = "111";
			list.set(i, string);
			i ++;
		}
		for (String string : list) {
			System.out.println(string);
		}
	}
}
