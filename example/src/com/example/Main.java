/*
Copyright (c) 2013 JAVAPAN
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions
are met:
1. Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright
   notice, this list of conditions and the following disclaimer in the
   documentation and/or other materials provided with the distribution.
3. Neither the name of the copyright holders nor the names of its
   contributors may be used to endorse or promote products derived from
   this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.example;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import javapan.ParameterNameReader;

public class Main {

	public Main(int foo) {
	}

	public void bar(int baz) {

	}

	public static class NestedClass {
		public NestedClass(String s) {

		}

		public void nestedMethod(char c) {
		}

	}

	public class InnerClass {

		public InnerClass(long l) {

		}

		public void innerMethod(byte b) {
		}
	}

	public static void main(String[] args) throws Exception {

		assertConstructorOf(Main.class)
			.withParams(int.class)
			.hasParameterNames("foo");

		assertMethodIn(Main.class, "bar")
			.withParams(int.class)
			.hasParameterNames("baz");

		assertConstructorOf(NestedClass.class)
			.withParams(String.class)
			.hasParameterNames("s");

		assertMethodIn(NestedClass.class, "nestedMethod")
			.withParams(char.class)
			.hasParameterNames("c");

		assertConstructorOf(InnerClass.class)
			.withParams(Main.class, long.class)
			.hasParameterNames("l");

		assertMethodIn(InnerClass.class, "innerMethod")
			.withParams(byte.class)
			.hasParameterNames("b");

		System.out.println("it works!");
	}

	private static ParamNameAssertion assertConstructorOf(Class<?> type) {
		ParamNameAssertion pna = new ParamNameAssertion();
		pna.type = type;
		return pna;
	}

	private static ParamNameAssertion assertMethodIn(Class<?> type, String methodName) {
		ParamNameAssertion pna = new ParamNameAssertion();
		pna.type = type;
		pna.methodName = methodName;
		return pna;
	}

	@SuppressWarnings("rawtypes")
	private static class ParamNameAssertion {
		private Class<?> type;
		private String methodName;

		private Class[] params;

		public ParamNameAssertion withParams(Class... params) {
			this.params = params;
			return this;
		}

		public void hasParameterNames(String... expected) throws Exception {
			if (methodName != null) {
				Method method = type.getMethod(methodName, params);
				List<String> parameterNames = ParameterNameReader.getParameterNames(method);
				assertEquals(parameterNames, expected);
			} else {
				Constructor<?> contor = type.getConstructor(params);
				List<String> parameterNames = ParameterNameReader.getParameterNames(contor);
				assertEquals(parameterNames, expected);
			}
		}
	}

	private static void assertEquals(List<String> a, String... b) {
		if (!Arrays.asList(b).equals(a)) {
			throw new IllegalStateException("not working... " + a);
		}
	}
}