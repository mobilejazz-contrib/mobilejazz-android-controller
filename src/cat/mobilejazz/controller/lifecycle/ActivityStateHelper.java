package cat.mobilejazz.controller.lifecycle;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Parcelable;

public final class ActivityStateHelper {

	private static void addFields(Class<?> clazz, List<Field> list) {
		for (Field f : clazz.getFields()) {
			list.add(f);
		}
		Class<?> superClass = clazz.getSuperclass();
		if (superClass != null) {
			addFields(superClass, list);
		}
	}

	public static List<Field> getAllFields(Class<?> clazz) {
		List<Field> result = new ArrayList<Field>();
		addFields(clazz, result);
		return result;
	}
	
	public static void writeObject(Bundle out, String key, Object value) {
		if (value instanceof Boolean) {
			out.putBoolean(key, (Boolean) value);
		} else if (value instanceof Character) {
			out.putChar(key, (Character) value);
		} else if (value instanceof CharSequence) {
			out.putCharSequence(key, (CharSequence) value);
		} else if (value instanceof Short) {
			out.putShort(key, (Short) value);
		} else if (value instanceof Integer) {
			out.putInt(key, (Integer) value);
		} else if (value instanceof Long) {
			out.putLong(key, (Long) value);
		} else if (value instanceof Float) {
			out.putFloat(key, (Float) value);
		} else if (value instanceof Double) {
			out.putDouble(key, (Double) value);
		} else if (value instanceof String) {
			out.putString(key, (String) value);
		} else if (value instanceof Parcelable) {
			out.putParcelable(key, (Parcelable) value);
		} else if (value instanceof Serializable) {
			out.putSerializable(key, (Serializable) value);
		}
	}

	public static void writeMembers(Bundle out, Object object) {
		Class<?> clazz = object.getClass();

		for (Field f : getAllFields(clazz)) {
			try {
				Object value = f.get(object);
				writeObject(out, f.getName(), value);
			} catch (IllegalArgumentException e) {
				// should not happen
			} catch (IllegalAccessException e) {
				// ignore
			}
		}
	}

	public static void readMembers(Bundle in, Object object) {
		Class<?> clazz = object.getClass();

		for (Field f : getAllFields(clazz)) {
			try {
				f.set(object, in.get(f.getName()));
			} catch (IllegalArgumentException e) {
				// should not happen
			} catch (IllegalAccessException e) {
				// ignore
			}
		}
	}

}
