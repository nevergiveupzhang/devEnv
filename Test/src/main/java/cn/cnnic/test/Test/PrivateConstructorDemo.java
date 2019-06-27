package cn.cnnic.test.Test;

import java.lang.reflect.Field;

public class PrivateConstructorDemo {

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, NoSuchFieldException, SecurityException {
		Bean bean=new Bean();
		bean.getClass().getField("");
		Field id=bean.getClass().getDeclaredField("id");
		id.setAccessible(true);
		id.set(bean, 10);
		System.out.println(bean.getId());
	}

}
