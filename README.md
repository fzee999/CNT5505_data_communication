# A Network Emulator
We consider broadcast Ethernet LANs, where stations (and routers)in a LAN are attached to a shared broadcast physical medium. In such a LAN network, a packet sent from any one station is broadcasted to and received by all the other stations in the same LAN

We consider a star-wired LAN topology where each station is directly connected to a common central node, called a transparent bridge or simply bridge (or switch). Each station is connected to the bridge through one of the bridge's ports, thus a bridge with n ports can connect n stations in total. When a bridge is first plugged, it broadcasts an Ethernet data frame it receives from one station (via one of its ports) to all the other stations attached to it, i.e., by re-transmitting the frame through all the ports except the one from which it receives the frame. This enables every station to hear the transmission from any other station connected to the bridge. Each frame has a frame header that includes the source and destination (next-hop) MAC addresses. Through self learning, the bridge gradually learns the location of the stations on the LAN and only forwards the frame to the necessary segment of the LAN. A station (and router) should accept a data frame sent by another station that is addressed to it (i.e., the destination MAC address is the address of the station or it is a broadcast MAC address). A station discards any data frames that are not addressed to it.

#Project Objectives
Mastering socket based networking application development
Understanding functionalities of various networking devices such as bridges/switches and routers
Understanding how packets are forwarded in networks
Experiencing team software development
Writing research-style papers

![image](https://github.com/fzee999/CNT5505_data_communication/assets/18706882/60457977-8865-45ea-8790-77d582bd5229)
Figure 1: A simple network topology

#In different xterm windows:

bridge cs1 8 (starting bridge 1 with name "cs1". It has 8 ports)  
bridge cs2 8 (second bridge)  
bridge cs3 8 (third bridge)  
station -route ifaces.r1 rtable.r1 hosts (first router. The interface information of the router is in file ifaces.r1. Routing table in rtable.r1. IP/hostname mapping in hosts)
station -route ifaces.r2 rtable.r2 hosts (second router)
station -no ifaces.a rtable.a hosts (first host; Host A)  
station -no ifaces.b rtable.b hosts (second host. Host B)
station -no ifaces.c rtable.c hosts (third host. Host C)  
station -no ifaces.d rtable.d hosts (last host. Host D)  
