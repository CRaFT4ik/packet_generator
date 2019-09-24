/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 + Copyright (c) 2019, Eldar Timraleev (aka CRaFT4ik).
 +
 + Licensed under the Apache License, Version 2.0 (the "License");
 + you may not use this file except in compliance with the License.
 + You may obtain a copy of the License at
 +
 +     http://www.apache.org/licenses/LICENSE-2.0
 +
 + Unless required by applicable law or agreed to in writing, software
 + distributed under the License is distributed on an "AS IS" BASIS,
 + WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 + See the License for the specific language governing permissions and
 + limitations under the License.
 +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

package ru.er_log.components;

import java.io.Serializable;

public class eEthernetConfig implements IConfig, Serializable
{
    private String srcMAC;
    private String dstMAC;

    public eEthernetConfig() {}

    public String getSrcMAC()
    {
        return srcMAC;
    }
    public eEthernetConfig setSrcMAC(String srcMAC)
    {
        this.srcMAC = srcMAC;
        return this;
    }
    public String getDstMAC()
    {
        return dstMAC;
    }
    public eEthernetConfig setDstMAC(String dstMAC)
    {
        this.dstMAC = dstMAC;
        return this;
    }
}
