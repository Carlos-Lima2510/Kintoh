# 🛡️ Kintoh

> Un monitor de salud para Kubernetes ligero, en tiempo real y tolerante a fallos, construido con Java y la API oficial de Kubernetes.

![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=flat&logo=openjdk&logoColor=white)
![Kubernetes](https://img.shields.io/badge/Kubernetes-Client-326CE5?style=flat&logo=kubernetes&logoColor=white)
![Build](https://img.shields.io/badge/build-Maven-C71A36?style=flat&logo=apache-maven&logoColor=white)

**Kintoh** es una herramienta de observabilidad que se conecta a tu clúster local (MicroK8s/Minikube) y vigila continuamente el ciclo de vida de los Pods. Su objetivo principal es la detección proactiva del temido estado `CrashLoopBackOff`, alertando en consola mucho antes de que un humano revise el dashboard.

[![Architecture](https://img.shields.io/badge/Architecture-View%20Public%20API-007EC6?style=for-the-badge&logo=uml&logoColor=white)](/documents/vistaPública.png) 

## Características Principales

* **Vigilancia en Tiempo Real:** Utiliza la API de `Watch` de Kubernetes para recibir eventos (streaming) en lugar de hacer polling constante.
* **Detección Inteligente:** Algoritmo de filtrado que ignora el ruido de arranque y solo alerta sobre fallos reales de contenedores.
* **Fail-Fast Connection:** La fábrica de clientes valida la conexión al inicio e impide ejecuciones zombies si el clúster no está disponible.

---

## Arquitectura del Proyecto

* **Domain (`/domain`):** El corazón de la aplicación. Contiene las interfaces puras y contratos (`Monitor`, `Watcher`, `Notifier`, `Resource`, `Event`).
* **Core / K8s (`/core` o `/k8s`):** Manejo de infraestructura y adaptadores. Convierte los objetos crudos de la API de Kubernetes en recursos de nuestro dominio (`K8sPodResource`, `K8sNodeResource`).
* **Watcher (`/watcher`):** Implementa el patrón *Template Method* (`AbstractK8sWatcher`) para gestionar hilos, resiliencia (reintentos) y ciclo de vida de forma centralizada.
* **Logic (`/logic`):** Implementaciones concretas de las reglas de negocio (ej. `PodCrashMonitor` para detectar `CrashLoopBackOff` y `NodeMonitor` para caídas de servidores).
* **Notifiers (`/notifiers`):** Capa de salida intercambiable. Se encarga de formatear y despachar los eventos generados (ej. `ConsoleNotifier`).

## Pre-requisitos

- **Java JDK 11 o superior.**

- **Maven 3.6+.**

- Un clúster de Kubernetes corriendo localmente (**MicroK8s, Minikube, Docker Desktop**).

## Guía de Inicio Rápido

1. **Construir el proyecto**

```bash
docker build . -t kintoh:v1
```

2. **Ejecutar el vigilante**

Para ejecutarlo fuera del cluster:

```bash
docker run -it --rm -v ~/.kube/config:/root/.kube/config kintoh:v1
```

3. **Resultado**

En la terminal de Kintoh deberías ver inmediatamente:

```text
--------------------------------------------------
    [CRÍTICO] DETECCIÓN DE ANOMALÍA 
    * Hora:    2026-02-21 23:35:42
    * Recurso: nginx-crash
    * Ámbito:  default
    * Info:    El contenedor 'nginx' ha entrado en estado CrashLoopBackOff
--------------------------------------------------
```
