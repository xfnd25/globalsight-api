az login

az group create --name rg-vmubuntu --location eastus

az network vnet create --resource-group rg-vmubuntu --name nnet-Linux --subnet-name subnet1

az network public-ip create --resource-group rg-vmubuntu --name pip-ubuntu

az network nsg create --resource-group rg-vmubuntu --name nsgsr-linux

az network nsg rule create --resource-group rg-vmubuntu --nsg-name nsgsr-linux --name Allow-SSH --protocol tcp --priority 1000 --destination-port-range 22 --access allow --direction inbound

az network nsg rule create --resource-group rg-vmubuntu --nsg-name nsgsr-linux --name Allow-App --protocol tcp --priority 1010 --destination-port-range 8080 --access allow --direction inbound

az vm create --resource-group rg-vmubuntu --name vm-ubuntu --image Canonical:ubuntu-24_04-lts:ubuntu-pro:latest --size Standard_B2s --vnet-name nnet-Linux --subnet subnet1 --nsg nsgsr-linux --public-ip-address pip-ubuntu --authentication-type password --admin-username admlnx --admin-password 'Fiap@2tdspZ@fFcp'

az vm show --resource-group rg-vmubuntu --name vm-ubuntu --show-details --query publicIps --output tsv

ssh admlnx@172.206.211.5

#Dentro da VM

sudo apt update && sudo apt upgrade -y

sudo apt install ca-certificates curl gnupg lsb-release -y

sudo mkdir -p /etc/apt/keyrings

curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg

echo
"deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg]
https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" |
sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

sudo apt update

sudo apt install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin -y

sudo docker version

sudo docker login

docker network create globalsight-network 

workdir 

docker run -d --name mysql-container --network globalsight-network -e MYSQL_ROOT_PASSWORD=senhaSegura123 -e MYSQL_DATABASE=meu_banco -e MYSQL_USER=usuario -e MYSQL_PASSWORD=senha123 -v mysql_data:/var/lib/mysql -p 3306:3306 mysql:8.0

docker run -d --name globalsight-api --network globalsight-network -p 8080:8080 xfnd25/globalsight-api:1.2

docker ps

docker exec -it mysql-container mysql -uusuario -psenha123 meu_banco
show tables;
select * from nome_tabela;

docker logs -f mysql-container

docker logs -f globalsight-api




