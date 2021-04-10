package com.main.glory.model.user;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Permissions {
	Boolean view;
	Boolean add;
	Boolean edit;
	Boolean delete;
	Boolean viewGroup;
	Boolean editGroup;
	Boolean deleteGroup;
	Boolean viewAll;
	Boolean editAll;
	Boolean deleteAll;
	//1111101010

	public static String toBinary(int x, int len) {
		if (len > 0) {
			return String.format("%" + len + "s",
					Integer.toBinaryString(x)).replaceAll(" ", "0");
		}
		return null;
	}

	public Permissions(Integer code) {
		view = add = edit = delete = viewGroup = editGroup = deleteGroup = viewAll = editAll = deleteAll = false;
		String binary = toBinary(code, 10);
		System.out.println("binary:"+binary);
		assert binary != null;
		if(binary.charAt(0) == '1'){
			view = true;
		}if(binary.charAt(1) == '1'){
			add = true;
		}if(binary.charAt(2) == '1'){
			edit = true;
		}if(binary.charAt(3) == '1'){
			delete = true;
		}if(binary.charAt(4) == '1'){
			viewGroup = true;
		}if(binary.charAt(5) == '1'){
			editGroup = true;
		}if(binary.charAt(6) == '1'){
			deleteGroup = true;
		}if(binary.charAt(7) == '1'){
			viewAll = true;
		}if(binary.charAt(8) == '1'){
			editAll = true;
		}if(binary.charAt(9) == '1'){
			deleteAll = true;
		}
	}
}
