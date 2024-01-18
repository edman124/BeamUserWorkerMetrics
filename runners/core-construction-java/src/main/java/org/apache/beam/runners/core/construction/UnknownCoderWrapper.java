/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.beam.runners.core.construction;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.beam.sdk.coders.AtomicCoder;
import org.apache.beam.sdk.coders.CoderException;

/**
 * Represents a {@coder Coder} that is not defined in Java SDK, for example, a coder that is
 * available in an external SDK that cannot be fully interpretted in the Java SDK.
 */
public class UnknownCoderWrapper extends AtomicCoder<Object> {
  private String urn;
  private byte[] payload;

  private UnknownCoderWrapper(String urn, byte[] payload) {
    this.urn = urn;
    this.payload = payload;
  }

  public static UnknownCoderWrapper of(String urn, byte[] payload) {
    return new UnknownCoderWrapper(urn, payload);
  }

  @Override
  public void encode(Object value, OutputStream outStream) throws CoderException, IOException {
    throw new CoderException(
        "`UnknownCoderWrapper` was used to perform an actual encoding in the Java SDK. "
            + "Potentially a `PCollection` that was generated by a cross-language transform, "
            + "that uses a coder that is not available in the Java SDK, is being consumed by a Java"
            + "transform. Please make sure that cross-language transforms at the language"
            + "boundary use Beam portable coders.");
  }

  @Override
  public Object decode(InputStream inStream) throws CoderException, IOException {
    throw new CoderException(
        "`UnknownCoderWrapper` was used to perform an actual decoding in the Java SDK. "
            + "Potentially a Java transform is being followed by a cross-language transform that"
            + "uses a coder that is not available in the Java SDK. Please make sure that Python "
            + "transforms at the multi-language boundary use Beam portable coders.");
  }

  public String getUrn() {
    return urn;
  }

  public byte[] getPayload() {
    return payload;
  }
}
