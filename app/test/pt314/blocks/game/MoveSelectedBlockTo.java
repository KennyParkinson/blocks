package pt314.blocks.game;


import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import pt314.blocks.gui.SimpleGUI;











import org.junit.Before;
import org.junit.Test;

public class MoveSelectedBlockTo {
	public SimpleGUI test;
	
	@Before 
	public void setUp() {
		test = new SimpleGUI();
	}
	@Test
	public void validMove() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
//		Method initBoard = SimpleGUI.class.getDeclaredMethod("initBoard", String.class);
//		initBoard.setAccessible(true);
//		initBoard.invoke(test, "puzzle-001.txt");
		Method select = SimpleGUI.class.getDeclaredMethod("selectBlockAt", int.class, int.class);
		Method moveTo = SimpleGUI.class.getDeclaredMethod("moveSelectedBlockTo", int.class, int.class);
		select.setAccessible(true);
		moveTo.setAccessible(true);
		
		select.invoke(test, 1,3);
		boolean result = (boolean) moveTo.invoke(test, 0, 3);
		select.invoke(test, 0,0);
		boolean result1 = (boolean) moveTo.invoke(test, 0, 2);
		assertTrue(result1 && result);
	}
	
	@Test
	public void moveThroughBlock() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method select = SimpleGUI.class.getDeclaredMethod("selectBlockAt", int.class, int.class);
		Method moveTo = SimpleGUI.class.getDeclaredMethod("moveSelectedBlockTo", int.class, int.class);
		select.setAccessible(true);
		moveTo.setAccessible(true);
		
		select.invoke(test, 1,3);
		moveTo.invoke(test, 0, 3);
		select.invoke(test, 0,0);
		moveTo.invoke(test, 0, 1);
		select.invoke(test, 0,1);
		moveTo.invoke(test, 0, 2);
		select.invoke(test, 0,2);
		boolean result1 = (boolean) moveTo.invoke(test, 0, 3);
		boolean result2 = (boolean) moveTo.invoke(test, 0,4);
		assertFalse(result1 && result2);
	}
	
	@Test
	public void moveOffScreen() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method select = SimpleGUI.class.getDeclaredMethod("selectBlockAt", int.class, int.class);
		Method moveTo = SimpleGUI.class.getDeclaredMethod("moveSelectedBlockTo", int.class, int.class);
		select.setAccessible(true);
		moveTo.setAccessible(true);
		
		select.invoke(test, 1,3);
		boolean result = (boolean) moveTo.invoke(test, -1, 3);
		select.invoke(test, 0,0);
		boolean result1 = (boolean) moveTo.invoke(test, 0, -2);
		assertFalse(result1 && result);
	}
	
	@Test
	public void invalidMove() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method select = SimpleGUI.class.getDeclaredMethod("selectBlockAt", int.class, int.class);
		Method moveTo = SimpleGUI.class.getDeclaredMethod("moveSelectedBlockTo", int.class, int.class);
		select.setAccessible(true);
		moveTo.setAccessible(true);
		
		select.invoke(test, 1,3);
		boolean result = (boolean) moveTo.invoke(test, 1, 4);
		select.invoke(test, 0,0);
		boolean result1 = (boolean) moveTo.invoke(test, 1, 0);
		assertFalse(result1 && result);
	}
	
	
	

}
