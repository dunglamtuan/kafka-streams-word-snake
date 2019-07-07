/*
 * Copyright 2018 Confluent Inc.
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
 */

package io.confluent.kafka.schemaregistry.client;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.avro.Schema;

/**
 * Mock implementation of SchemaRegistryClient that can be used for tests. This version is NOT
 * thread safe. Schema data is stored in memory and is not persistent or shared across instances.
 */
public class MockSchemaRegistryClient implements SchemaRegistryClient {

  private final Map<Integer, Schema> schemaIdCache;
  private final Map<Integer, String> subjectIdCache;

  private static AtomicInteger currentIndex = new AtomicInteger(1);

  public MockSchemaRegistryClient(Map<String, Schema> topics) {
    schemaIdCache = new HashMap<>();
    subjectIdCache = new HashMap<>();

    topics.forEach((key, value) -> {
      schemaIdCache.put(currentIndex.get(), value);
      subjectIdCache.put(currentIndex.get(), key);
      currentIndex.getAndIncrement();
    });
  }

  private int getIdFromRegistry(String subject) {
    return subjectIdCache.entrySet().stream()
        .filter(k -> k.getValue().equalsIgnoreCase(subject))
        .findAny()
        .get()
        .getKey();
  }

  @Override
  public synchronized List<Integer> getAllVersions(String subject) {
    return Collections.singletonList(1);
  }

  private Schema getSchemaBySubjectAndIdFromRegistry(String subject, int id) {
    int idFromRegistry = getIdFromRegistry(subject);
    return schemaIdCache.get(idFromRegistry);
  }

  @Override
  public synchronized int register(String subject, Schema schema) {
    schemaIdCache.put(currentIndex.get(), schema);
    subjectIdCache.put(currentIndex.get(), subject);
    return currentIndex.getAndIncrement();
  }

  @Override
  public Schema getByID(final int id) {
    return schemaIdCache.get(id);
  }

  @Override
  public synchronized Schema getById(int id) {
    return schemaIdCache.get(id);
  }

  @Override
  public Schema getBySubjectAndID(final String subject, final int id) {
    return getSchemaBySubjectAndIdFromRegistry(subject, id);
  }

  @Override
  public synchronized Schema getBySubjectAndId(String subject, int id) {
    return getSchemaBySubjectAndIdFromRegistry(subject, id);
  }

  @Override
  public synchronized SchemaMetadata getSchemaMetadata(String subject, int version) {
    return null;
  }

  @Override
  public synchronized SchemaMetadata getLatestSchemaMetadata(String subject) {
    return null;
  }

  @Override
  public synchronized int getVersion(String subject, Schema schema) {
    return 1;
  }

  @Override
  public boolean testCompatibility(String subject, Schema newSchema) {
    return true;
  }

  @Override
  public String updateCompatibility(String subject, String compatibility) {
    return null;
  }

  @Override
  public String getCompatibility(String subject) {
    return null;
  }

  @Override
  public Collection<String> getAllSubjects() {
    return Collections.emptyList();
  }

  @Override
  public int getId(String subject, Schema schema) {
    return getIdFromRegistry(subject);
  }

  @Override
  public List<Integer> deleteSubject(String s) {
    return null;
  }

  @Override
  public List<Integer> deleteSubject(Map<String, String> map, String s) {
    return null;
  }

  @Override
  public Integer deleteSchemaVersion(String s, String s1){
    return null;
  }

  @Override
  public Integer deleteSchemaVersion(Map<String, String> map, String s, String s1) {
    return null;
  }
}