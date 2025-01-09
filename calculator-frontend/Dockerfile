# calculator-frontend/Dockerfile

# Step 1: Build the Angular app
FROM node:lts-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm install

COPY . . 
RUN npm run build

FROM nginx:alpine
RUN rm -rf /usr/share/nginx/html/*

COPY --from=build /app/dist/calculator-frontend/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf  

EXPOSE 80
ENTRYPOINT ["nginx", "-g", "daemon off;"]
