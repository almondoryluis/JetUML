/*******************************************************************************
 * JetUML - A desktop application for fast UML diagramming.
 *
 * Copyright (C) 2022 by McGill University.
 * 
 * See: https://github.com/prmr/JetUML
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.jetuml.persistence.json;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * An object able to write a JSON value to its String representation.
 */
public final class JsonWriter
{
	private static final Map<Class<?>, Function<Object, String>> WRITERS = new IdentityHashMap<>();
	
	static
	{
		WRITERS.put(Boolean.class, Object::toString);
		WRITERS.put(Integer.class, Object::toString);
		WRITERS.put(String.class, JsonStringParser::writeJsonString);
		WRITERS.put(JsonObject.class, JsonObjectParser::writeJsonObject);
		WRITERS.put(JsonArray.class, JsonArrayParser::writeJsonArray);
	}
	
	/**
	 * @param pJsonValue A value to serialize.
	 * @return A serialized version of the input.
	 * @throws JsonException if pJsonValue is null or not a reference 
	 * to a valid instance of a JSON value.
	 */
	public static String write(Object pJsonValue)
	{
		JsonValueValidator.validateType(pJsonValue);
		return WRITERS.get(pJsonValue.getClass()).apply(pJsonValue);
	}
}
