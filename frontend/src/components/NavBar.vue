<template>
  <nav class="navbar navbar-expand-lg navbar-light">
    <div class="container">
      <router-link class="navbar-brand" to="/">
        <img src="/images/logo-celnet.png" alt="Logo" class="img-fluid" style="height: 40px;">
      </router-link>
      <button class="navbar-toggler" type="button" @click="toggleMenu" aria-controls="navbarNav" aria-expanded="false"
        aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" :class="{ show: menuAbierto }" id="navbarNav">
        <ul class="navbar-nav ms-auto">
          <li class="nav-item">
            <router-link class="nav-link" to="/">Inicio</router-link>
          </li>

          <!-- Dropdown para Cargar Excel y Recepción Contador -->
          <li class="nav-item dropdown" @click="toggleExcelRecepcionDropdown($event)">
            <a class="nav-link dropdown-toggle" href="#" role="button" aria-expanded="false">
              Procesos
            </a>
            <ul class="dropdown-menu" :class="{ show: excelRecepcionDropdownAbierto }">
              <li>
                <a class="dropdown-item" @click="navigate('/excel')">Cargar Excel</a>
              </li>
              <li>
                <a class="dropdown-item" @click="navigate('/recepcion')">Recepción Contador</a>
              </li>
            </ul>
          </li>

          <!-- Dropdown para Recuperados y Chatarra -->
          <li class="nav-item dropdown" @click="toggleRecuperadosChatarraDropdown($event)">
            <a class="nav-link dropdown-toggle" href="#" role="button" aria-expanded="false">
              Estado Contadores
            </a>
            <ul class="dropdown-menu" :class="{ show: recuperadosChatarraDropdownAbierto }">
              <li>
                <a class="dropdown-item" @click="navigate('/recuperado')">Recuperados</a>
              </li>
              <li>
                <a class="dropdown-item" @click="navigate('/achatarrado')">Chatarra</a>
              </li>
            </ul>
          </li>

          <!-- Dropdown para Tablas -->
          <li class="nav-item dropdown" @click="toggleDropdown($event)">
            <a class="nav-link dropdown-toggle" href="#" role="button" aria-expanded="false">
              Tablas
            </a>
            <ul class="dropdown-menu" :class="{ show: dropdownAbierto }" aria-labelledby="navbarDropdown">
              <li>
                <a class="dropdown-item" @click="navigate('/almacenes')">Almacen</a>
              </li>
              <li>
                <a class="dropdown-item" @click="navigate('/diagnostico')">Diagnostico</a>
              </li>
              <li>
                <a class="dropdown-item" @click="navigate('/distribuidora')">Distribuidora</a>
              </li>
              <li>
                <a class="dropdown-item" @click="navigate('/fabricante')">Fabricante</a>
              </li>
              <li>
                <a class="dropdown-item" @click="navigate('/modelo')">Modelo</a>
              </li>
              <li>
                <a class="dropdown-item" @click="navigate('/fichero')">Fichero</a>
              </li>
              <li>
                <a class="dropdown-item" @click="navigate('/proceso')">Proceso</a>
              </li>
              <li>
                <a class="dropdown-item" @click="navigate('/general')">General</a>
              </li>
            </ul>
          </li>
          <li class="nav-item">
            <router-link class="nav-link" to="/lotes">Lotes</router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link" to="/asignar">Asignar lote</router-link>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>

<script>
export default {
  name: "navbarComponent",
  data() {
    return {
      menuAbierto: false,
      dropdownAbierto: false,
      excelRecepcionDropdownAbierto: false,
      recuperadosChatarraDropdownAbierto: false,
    };
  },
  methods: {
    toggleMenu() {
      this.menuAbierto = !this.menuAbierto;
    },
    toggleDropdown(event) {
      this.dropdownAbierto = !this.dropdownAbierto;
      // Cerrar otros dropdowns si están abiertos
      this.excelRecepcionDropdownAbierto = false;
      this.recuperadosChatarraDropdownAbierto = false;
      event.stopPropagation(); // Evita que el clic se propague
    },
    toggleExcelRecepcionDropdown(event) {
      this.excelRecepcionDropdownAbierto = !this.excelRecepcionDropdownAbierto;
      // Cerrar otros dropdowns si están abiertos
      this.dropdownAbierto = false;
      this.recuperadosChatarraDropdownAbierto = false;
      event.stopPropagation(); // Evita que el clic se propague
    },
    toggleRecuperadosChatarraDropdown(event) {
      this.recuperadosChatarraDropdownAbierto = !this.recuperadosChatarraDropdownAbierto;
      // Cerrar otros dropdowns si están abiertos
      this.dropdownAbierto = false;
      this.excelRecepcionDropdownAbierto = false;
      event.stopPropagation(); // Evita que el clic se propague
    },
    navigate(route) {
      this.excelRecepcionDropdownAbierto = false;
      this.recuperadosChatarraDropdownAbierto = false;
      this.dropdownAbierto = false;
      this.$router.push(route);
    },
    handleClickOutside(event) {
      const dropdowns = [
        this.$el.querySelector('.navbar-nav .dropdown-menu')
      ];
      // Si el clic es fuera del dropdown, cerrar los dropdowns
      for (let i = 0; i < dropdowns.length; i++) {
        if (!dropdowns[i].contains(event.target)) {
          this.closeAllDropdowns();
        }
      }
    },
    closeAllDropdowns() {
      this.excelRecepcionDropdownAbierto = false;
      this.recuperadosChatarraDropdownAbierto = false;
      this.dropdownAbierto = false;
    }
  },
  mounted() {
    // Escuchar el clic fuera del navbar cuando el componente se monte
    document.addEventListener('click', this.handleClickOutside);
  },
  beforeDestroy() {
    // Eliminar el listener cuando el componente se destruya
    document.removeEventListener('click', this.handleClickOutside);
  }
};
</script>

<style scoped>

.navbar {
  background-color: #274469;
  padding: 10px 20px;
}

.container {
  width: 100%;
  max-width: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.navbar-brand img {
  height: 50px;
  max-height: 100%;
}

.navbar-nav {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
}

.navbar-nav .nav-item {
  margin-left: 15px;
}

.navbar-nav .nav-link {
  color: #ffffff;
  font-size: 18px;
  text-transform: uppercase;
  padding: 10px 15px;
}

.navbar-nav .nav-link:hover {
  color: #6c757d;
}

.dropdown-menu {
  background-color: #ffffff;
  border-radius: 0.375rem;
}

.dropdown-item {
  color: #274469;
}

.dropdown-item:hover {
  background-color: #f8f9fa;
}

@media (max-width: 768px) {
  .navbar {
    flex-wrap: wrap;
  }

  .navbar-brand {
    margin-bottom: 10px;
  }

  .navbar-nav {
    flex-direction: column;
    align-items: flex-start;
    width: 100%;
  }

  .nav-item {
    width: 100%;
    text-align: left;
  }

  .navbar-toggler {
    margin-left: auto;
  }
}
</style>