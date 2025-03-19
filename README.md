<img  src = "https://github.com/user-attachments/assets/30c2cc2d-2352-4886-b44e-b2978f64239e" style="height: 500px; width:690px;"/>

<!-- These are statistics for this repository-->
[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![Apache License][license-shield]][license-url]


# Agama Magiclink Project

Magic Link authentication allows users to log in securely without a password. This implementation is designed for Gluu Server and Jans Server, providing a seamless authentication experience using a one-time link sent via email.

## Where To Deploy

The project can be deployed to any IAM server that runs an implementation of 
the [Agama Framework](https://docs.jans.io/head/agama/introduction/) like 
[Janssen Server](https://jans.io) and [Gluu Flex](https://gluu.org/flex/).

## How To Deploy

Different IAM servers may provide different methods and 
user interfaces from where an Agama project can be deployed on that server. 
The steps below show how the Agama-SMTP project can be deployed on the 
[Janssen Server](https://jans.io). 

Deployment of an Agama project involves three steps.

- [Downloading the `.gama` package from the project repository](#download-the-project)
- [Adding the `.gama` package to the IAM server](#add-the-project-to-the-server)
- [Configure the project](#configure-the-project)


#### Pre-Requisites

To send email messages, ensure you have the Jans Auth Server with 
[SMTP service](https://docs.jans.io/head/admin/config-guide/smtp-configuration/)
configured


### Download the Project

> [!TIP]
> Skip this step if you use the Janssen Server TUI tool to 
> configure this project. The TUI tool enables the download and adding of this 
> project directly from the tool, as part of the `community projects` listing. 


The project is bundled as 
[.gama package](https://docs.jans.io/head/agama/gama-format/). 
Visit the `Assets` section of the 
[Releases](https://github.com/GluuFederation/agama-magiclink/releases) to download the `.gama` package.


### Add The Project To The Server

The Janssen Server provides multiple ways an Agama project can be 
deployed and configured. Either use the command-line tool, REST API, or a To send email messages, ensure you have the Jans Auth Server set up. It includes an SMTP service for sending emails, but you need to configure it before use.
TUI (text-based UI). Refer to 
[Agama project configuration page](https://docs.jans.io/head/admin/config-guide/auth-server-config/agama-project-configuration/) 
in the Janssen Server documentation for more details.

### Configure The Project

The Agama project accepts configuration parameters in the JSON format. 
Every Agama project comes with a basic sample configuration file for reference.


Below is a typical configuration of the Agama-Magiclink project. As shown, it contains
configuration parameters for the [flows contained in it](#flows-in-the-project):

```
{
  "org.gluu.agama.magiclink": {
    "hostName": "<host-name>",
    "secretKey": "your-secreat-key",
    "tokenExpiration": 10
  }
}
```

### Create SECRET_KEY
> [!TIP]
> `openssl rand -base64 32` 



### Test The Flow

Use any relying party implementation (like [jans-tarp](https://github.com/JanssenProject/jans/tree/main/demos/jans-tarp)) 
to send an authentication request that triggers the flow.

From the incoming authentication request, the Janssen Server reads the `ACR` 
parameter value to identify which authentication method should be used. 
To invoke the `org.gluu.agama.magiclink` flow contained in the Agama-magiclink project, 
specify the ACR value as `agama_<qualified-name-of-the-top-level-flow>`, 
i.e `agama_org.gluu.agama.magiclink`.


## Customize and Make It Your Own

Fork this repo to start customizing the Agama-magiclink project. It is possible to 
customize the user interface provided by the flow to suit your organization's 
branding guidelines. Or customize the overall flow behavior. Follow the best 
practices and steps listed [here](https://docs.jans.io/head/admin/developer/agama/agama-best-practices/#project-reuse-and-customizations)
to achieve these customizations in the best possible way.
This project can be reused in other Agama projects to create more complex
authentication journeys. To reuse, trigger the 
[org.gluu.agama.magiclink](#orggluuagamamagiclink) flow from other Agama projects.

To make it easier to visualize and customize the Agama Project, use
[Agama Lab](https://cloud.gluu.org/agama-lab/login).

## Flows In The Project

List of the flows: 

- [org.gluu.agama.magiclink](#orggluuagamamagiclink)

### org.gluu.agama.magiclink

The main flow of this project is [org.gluu.agama.magiclink](./code/org.gluu.agama.magiclink.flow) .
In step one, the person enters their email address, to which the IDP sends an token.
After token verification,  the flow is successful.


# Sequence Diagram

A basic diagram to understand how the `agama-magiclink` works.

```mermaid
sequenceDiagram

title Agama-magiclink Authentication Flow

actor User
participant "Client App" as Client
participant "Auth Server" as Server
participant "Email Service" as Email
participant "User's Email" as Mailbox

User -> Client: Request Login
Client -> Server: Generate Magic Link
Server -> Email: Send Magic Link to User's Email
Email -> Mailbox: Deliver Email with Magic Link

User -> Mailbox: Open Email
User -> Client: Click Magic Link
Client -> Server: Validate Token
alt Token Valid
    Server -> Client: Authenticate User
    Client -> User: Redirect to Dashboard
else Token Expired/Invalid
    Server -> Client: Show Error
    Client -> User: Display Error Message
end

```


# Demo

https://github.com/user-attachments/assets/38694b12-5dbd-4a06-89be-ae26e11ab161





<!-- This are stats url reference for this repository -->
[contributors-shield]: https://img.shields.io/github/contributors/GluuFederation/agama-magiclink.svg?style=for-the-badge
[contributors-url]: https://github.com/GluuFederation/agama-magiclink/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/GluuFederation/agama-magiclink.svg?style=for-the-badge
[forks-url]: https://github.com/GluuFederation/agama-magiclink/network/members
[stars-shield]: https://img.shields.io/github/stars/GluuFederation/agama-magiclink?style=for-the-badge
[stars-url]: https://github.com/GluuFederation/agama-magiclink/stargazers
[issues-shield]: https://img.shields.io/github/issues/GluuFederation/agama-magiclink.svg?style=for-the-badge
[issues-url]: https://github.com/GluuFederation/agama-magiclink/issues
[license-shield]: https://img.shields.io/github/license/GluuFederation/agama-magiclink.svg?style=for-the-badge
[license-url]: https://github.com/GluuFederation/agama-magiclink/blob/main/LICENSE
