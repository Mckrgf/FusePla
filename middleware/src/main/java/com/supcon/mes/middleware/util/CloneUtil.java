package com.supcon.mes.middleware.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class CloneUtil {

	/**
	 * 深度克隆
	 * 
	 * @param obj
	 *            源数据
	 * @return 克隆出来的数据
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static <T> T clone(T obj) throws Exception {
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		ObjectOutputStream oo = new ObjectOutputStream(bo);
		oo.writeObject(obj);// 从流里读出来
		ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
		ObjectInputStream oi = new ObjectInputStream(bi);
		return (T) oi.readObject();
	}
}
