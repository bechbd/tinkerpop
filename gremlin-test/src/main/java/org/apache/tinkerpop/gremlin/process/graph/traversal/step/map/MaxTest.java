/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.tinkerpop.gremlin.process.graph.traversal.step.map;

import org.apache.tinkerpop.gremlin.LoadGraphWith;
import org.apache.tinkerpop.gremlin.process.AbstractGremlinProcessTest;
import org.apache.tinkerpop.gremlin.process.Scope;
import org.apache.tinkerpop.gremlin.process.Traversal;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.apache.tinkerpop.gremlin.LoadGraphWith.GraphData.MODERN;
import static org.apache.tinkerpop.gremlin.process.graph.traversal.__.*;
import static org.junit.Assert.*;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public abstract class MaxTest extends AbstractGremlinProcessTest {

    public abstract Traversal<Vertex, Integer> get_g_V_age_max();

    public abstract Traversal<Vertex, Integer> get_g_V_repeatXbothX_timesX5X_age_max();

    public abstract Traversal<Vertex, Map<String, Number>> get_g_V_hasLabelXsoftwareX_group_byXnameX_byXbothE_valuesXweightX_foldX_byXmaxXlocalXX();

    @Test
    @LoadGraphWith(MODERN)
    public void g_V_age_max() {
        final List<Traversal<Vertex, Integer>> traversals = Arrays.asList(
                get_g_V_age_max(),
                get_g_V_repeatXbothX_timesX5X_age_max());
        traversals.forEach(traversal -> {
            printTraversalForm(traversal);
            checkResults(Arrays.asList(35), traversal);
        });
    }

    @Test
    @LoadGraphWith(MODERN)
    public void g_V_hasLabelXsoftwareX_group_byXnameX_byXin_valuesXageX_foldX_byXmaxXlocalXX() {
        final Traversal<Vertex, Map<String, Number>> traversal = get_g_V_hasLabelXsoftwareX_group_byXnameX_byXbothE_valuesXweightX_foldX_byXmaxXlocalXX();
        printTraversalForm(traversal);
        assertTrue(traversal.hasNext());
        final Map<String, Number> map = traversal.next();
        assertFalse(traversal.hasNext());
        assertEquals(2, map.size());
        assertEquals(1.0, map.get("ripple"));
        assertEquals(0.4, map.get("lop"));
    }

    public static class StandardTest extends MaxTest {

        @Override
        public Traversal<Vertex, Integer> get_g_V_age_max() {
            return g.V().values("age").max();
        }

        @Override
        public Traversal<Vertex, Integer> get_g_V_repeatXbothX_timesX5X_age_max() {
            return g.V().repeat(both()).times(5).values("age").max();
        }

        @Override
        public Traversal<Vertex, Map<String, Number>> get_g_V_hasLabelXsoftwareX_group_byXnameX_byXbothE_valuesXweightX_foldX_byXmaxXlocalXX() {
            return g.V().hasLabel("software").group().by("name").by(bothE().values("weight").fold()).by(max(Scope.local)).cap();
        }
    }

    public static class ComputerTest extends MaxTest {

        @Override
        public Traversal<Vertex, Integer> get_g_V_age_max() {
            return g.V().values("age").<Integer>max();
        }

        @Override
        public Traversal<Vertex, Integer> get_g_V_repeatXbothX_timesX5X_age_max() {
            return g.V().repeat(both()).times(5).values("age").<Integer>max();
        }

        @Override
        public Traversal<Vertex, Map<String, Number>> get_g_V_hasLabelXsoftwareX_group_byXnameX_byXbothE_valuesXweightX_foldX_byXmaxXlocalXX() {
            return g.V().hasLabel("software").group().by("name").by(bothE().values("weight").fold()).
                    by(max(Scope.local)).<Map<String, Number>>cap();
        }
    }
}