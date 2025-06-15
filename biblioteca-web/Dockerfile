# Stage 1: build com Node
FROM node:18-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
# build Vite (em tsconfig, define output em /dist)
RUN npm run build

# Stage 2: serve estático com Nginx
FROM nginx:alpine
# remove configuração default do Nginx
RUN rm /etc/nginx/conf.d/default.conf
# cópia do seu Nginx custom (opcional) ou use o default:
COPY nginx.conf /etc/nginx/conf.d/
COPY --from=build /app/dist /usr/share/nginx/html

ARG WEB_PORT
ENV PORT=${WEB_PORT}
EXPOSE ${WEB_PORT}

CMD ["nginx","-g","daemon off;"]
