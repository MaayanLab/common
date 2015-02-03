package edu.mssm.pharm.maayanlab.common.math;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class NumberUtilsTest extends TestCase {

	public static Test suite() {
		return new TestSuite(NumberUtilsTest.class);
	}
	
	public void testRoundUpDivision() {
		assertEquals(1, NumberUtils.roundUpDivision(3, 3));
		assertEquals(2, NumberUtils.roundUpDivision(4, 3));
		assertEquals(2, NumberUtils.roundUpDivision(5, 3));
		assertEquals(2, NumberUtils.roundUpDivision(6, 3));
		assertEquals(3, NumberUtils.roundUpDivision(7, 3));
	}
	
}
