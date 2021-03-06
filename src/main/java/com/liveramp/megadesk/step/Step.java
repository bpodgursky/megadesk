/**
 *  Copyright 2012 LiveRamp
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.liveramp.megadesk.step;

import com.liveramp.megadesk.condition.Condition;
import com.liveramp.megadesk.condition.ConditionWatcher;
import com.liveramp.megadesk.dependency.Dependency;
import com.liveramp.megadesk.resource.Resource;

import java.util.List;

public interface Step {

  public String getId();

  public List<Condition> conditions();

  public List<Dependency> dependencies();

  public List<Resource> writes();

  public boolean acquire(ConditionWatcher watcher) throws Exception;

  public void release() throws Exception;

  public void write(Resource resource, Object data) throws Exception;

  boolean isReady(ConditionWatcher watcher) throws Exception;

  public void run() throws Exception;
}
