/*
 * Copyright (c) 2013 matheusdev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.matheusdev.ror.view;

import org.matheusdev.ror.ResourceLoader;
import org.matheusdev.util.JsonDOM.JsonObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author matheusdev
 */
public final class EntityViews
{

	private final Map<String, EntityViewFactory> factories = new HashMap<>();

	public EntityViews()
	{
		addFactory(ViewWalking.Factory.get());
	}

	public void addFactory(EntityViewFactory factory)
	{
		factories.put(factory.getName(), factory);
	}

	public EntityView createView(String name, ResourceLoader res, JsonObject conf)
	{
		EntityViewFactory factory = factories.get(name);
		if(factory == null)
		{
			throw new UnkownViewException("Unkown view: " + name);
		}

		return factory.make(res, conf);
	}

}
