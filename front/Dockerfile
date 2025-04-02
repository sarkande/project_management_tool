# Étape 1 : Build
FROM node:20 AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build  # Génère dist/project_management_tool avec browser/ et server/

# Étape 2 : Serveur web (Nginx pour la partie statique)
FROM nginx:alpine
# On copie uniquement le contenu du dossier "browser" qui contient les fichiers statiques
COPY --from=build /app/dist/project_management_tool/browser /usr/share/nginx/html
# Configuration Nginx pour gérer les routes Angular
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
