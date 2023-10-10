//Name: Faraz Ahmad
//FSUID: fa21i@fsu.edu
#include<stdio.h>
#include<stdlib.h>
#include<sys/types.h>
#include<sys/socket.h>
#include<netinet/in.h>
#include<unistd.h>
#include<string.h>

#include <arpa/inet.h>

int error_check;
int server_socket;
int connection_socket;
struct sockaddr_in server_address;
struct sockaddr_in client_address;
char connection_msg[256];
fd_set descp_set;
int connections[10];

void print_connections(){
    for (int i = 0; i < 10; i++)
    {
        printf("conn %d: %d\n",i,connections[i]);
    }
    
}
void server_setup(){


    //creating server socket
    
    server_socket=socket(AF_INET,SOCK_STREAM,0);//SOCK_STREAM is for tcp
    if(server_socket==-1)
        perror("error in socket creation");

    //defining server's address
    
    socklen_t len = sizeof(server_address);
    server_address.sin_family=AF_INET;          //ipv4
    server_address.sin_port=0;       //port to connect to
    server_address.sin_addr.s_addr=INADDR_ANY;  //this effectively gives 0


    //binding the socket to our specified IP & port
    error_check=bind(server_socket,(struct sockaddr*)&server_address,sizeof(server_address));
    if (error_check==-1)
        perror("error in binding");

    
    //listening for client sockets
    error_check=listen(server_socket,5);
    //5 is the max no of allowed sockets
    if(error_check==-1)	
        perror("error in listening\n");

        
    if (getsockname(server_socket, (struct sockaddr*) &server_address, &len) != 0) {
        perror("Error in getsockname()");
    }
    printf("sin family:\t%d\n", server_address.sin_family);
    printf("Port Number:\t%d\n", server_address.sin_port);
    printf("address:\t%d\n", server_address.sin_addr.s_addr);

    for (int i = 0; i < 10; i++)
    {
        connections[i] = -1;
    }

}

int accept_connections(){
    
    //fetching client's address to establish connection
    socklen_t client =sizeof(client_address);    
    connection_socket=accept(server_socket,(struct sockaddr*)&client_address,&client);
    if(connection_socket==-1){
        perror("error in establishing connection with the client\n");
        return -1;
    }
    else{
        printf("Client connected: %s:%d\n", inet_ntoa(client_address.sin_addr), ntohs(client_address.sin_port));
        for (int i = 0; i < 10; i++)
        {
            if(connections[i]==-1){
                connections[i] = connection_socket;
                break;
            }
        }
        // print_connections();
        return 0;
    }
        
}


int main(){
    struct timeval timeout;
    
    server_setup();
    

    
    while(1){
        FD_ZERO(&descp_set);//to remove all file descriptors from the set
        FD_SET(server_socket,&descp_set);
		FD_SET(0,&descp_set);//setting the bit of the standard input descriptor

        for (int i = 0; i < 10; i++)
        {
            int sd = connections[i];
            if(sd!=-1)
                FD_SET(sd, &descp_set);
        }
        
         // set timeout
        timeout.tv_sec = 0;
        timeout.tv_usec = 500000;

        error_check=select(10,&descp_set,0,0,0);
        if(error_check==-1){
            printf("error in select operation");
            close(server_socket);
            close(connection_socket);
            return 0;
        }
        else if(FD_ISSET(server_socket,&descp_set)){
            // printf("check seversock\n");
            connection_socket = accept_connections();
            if(connection_socket > 0 ){
                FD_SET(connection_socket,&descp_set);
                // printf("is set\n");
            }
            
        }
        
        //checking if standard input could be read
        else if(FD_ISSET(0,&descp_set)){
            // printf("check sdin\n");
            fgets(connection_msg,sizeof(connection_msg),stdin);//get the message to give to the client from the terminal
            for (int i = 0; i < 10; i++)
            {
                if (connections[i]!=-1)
                {
                    int cs = connections[i];
                    error_check=send(cs,&connection_msg,sizeof(connection_msg),0);
                    if(error_check==-1){
                        perror("error msg: error in sending message to client\n\n");
                        return 0;
                    }
                    if(strncmp(connection_msg,"bye",3)==0){
                        close(server_socket);
                        return 0;
                    }
                }
                
            }
        }

        // else if(FD_ISSET(connection_socket,&descp_set)){
        //checking if connection socket could be read
        // printf("check4\n");
        for (int i = 0; i < 10; i++)
        {   
            if(connections[i]!=-1){
                int connection_socket = connections[i];
                printf("check2: %d\n",FD_ISSET(connection_socket,&descp_set));
                if(FD_ISSET(connection_socket,&descp_set)){
                    // printf("hello\n");
                    error_check=recv(connection_socket,&connection_msg,sizeof(connection_msg),0);
                    if(error_check==-1){
                        perror("error msg:msg wasnt recieved from client\n");
                        return 0;
                    }
                    if (strncmp(connection_msg,"bye",3)==0)
                    {
                        close(server_socket);
                        close(connection_socket);
                        return 0;
                    }
                    printf("cli%d-%s\n",connection_socket,connection_msg);
                    for (int i = 0; i < 10; i++)
                    {
                        if (connections[i]!=-1 && connections[i]!=connection_socket)
                        {
                            int cs = connections[i];
                            error_check=send(cs,&connection_msg,sizeof(connection_msg),0);
                            if(error_check==-1){
                                perror("error msg: error in sending message to client\n\n");
                                return 0;
                            }
                            if(strncmp(connection_msg,"bye",3)==0){
                                close(server_socket);
                                return 0;
                            }
                        }
                    }
                    
                    break;
                }
            }
            
        }
        // }
    }
    close(server_socket);
    close(connection_socket);
    return 0;
}