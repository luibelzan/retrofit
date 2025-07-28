<template>
  <div class="app-container">
    <!-- Navbar siempre visible -->
    <NavBar />
    
    <div class="body">
      <router-view />
    </div>
    
    <!-- Footer que se mueve con el contenido -->
    <Footer />
  </div>
</template>

<script>
import NavBar from './components/NavBar.vue';
import Footer from './components/Footer.vue';

export default {
  name: 'App',
  components: {
    NavBar,
    Footer,
  },
}
</script>

<style>
.app-container {
  display: flex;
  flex-direction: column;
}

.body {
  flex: 1; 
  padding: 20px; 
  background-color: #ffffff; 
}
</style>
