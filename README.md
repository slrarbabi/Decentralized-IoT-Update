# Decentralized-IoT-Update
When technology is centralized, it typically means that it is controlled and run by a single company, government, or individual. Decentralized technology on the other hand, is run by a network of participants that no one actor can control or shut down. With centralized technology gaining popularity, it is critical to highlight the resulting risks. These risks include but are not limited to issues of the loss of control over data, privacy concerns and dependence on the server or cloud computing providers and availability.
The Internet of Things (IoT) network of connected devices currently contains more than 11 billion devices and is anticipated to double in size within the next four years. The prevalence of these devices makes them an ideal target for attackers. To reduce the risk of attacks vendors routinely deliver security updates for their devices. The delivery of security updates becomes challenging due to the issue of scalability as the number of devices may grow much quicker than vendors' distribution systems. Which results in rising concerns over the availability of the centralized systems to provide adequate resources for vendors to distribute updates.
In this project, we propose a novel system to decentrally distribute digital content in a P2P network based on blockchain technology and smart contracts to address the concerns mentioned above. Additionally, in order to prevent the issues stemming from the free-riding challenge in P2P networks, in which peers would not generously share their resources to distribute content among other nodes, we will consider a Nash equilibrium micro-payment mechanism to grant adequate incentive for peers to participate in distributing data.

## Some links for more in depth learning
* [Learn Solidity.](https://www.git-tower.com/learn/ebook) A Turing-complete contract-oriented programming language to develope smart contracts
* [web3.js](https://web3js.readthedocs.io/en/1.0/)  - Ethereum JavaScript API

## Installing
* Install [Solidity](https://solidity.readthedocs.io/en/v0.4.24/installing-solidity.html)
* Install [Ganache](https://truffleframework.com/docs/ganache/quickstart): a personal blockchain for Ethereum development you can use to deploy contracts, develop your applications, and run tests.
```
Clone the repository and open the Java Network Simulator in IntelliJ. If you are using Maven, you do not need to worry about dependencies.
```
```
Deploy the smart contracts on Remix web-IDE and connect it to Ganache by selecting the "web-3 provider" as your environment.
```
After deploying your contracts, you will need to copy your account address and update the contract ABI (if modified contracts).
Run index.html to use the application
