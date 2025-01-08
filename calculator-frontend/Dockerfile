# calculator-frontend/Dockerfile

# Step 1: Build the Angular app
FROM node:lts-alpine as build
WORKDIR /app
COPY package*.json ./
RUN npm install

COPY . . 
RUN npm run build

# Step 2: Serve with NGINX
FROM nginx:alpine
COPY --from=build /app/dist/calculator-frontend /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
