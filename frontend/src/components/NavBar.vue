<template>

    <nav class="navbar navbar-expand-lg navbar-light">
        <div class="container">
            <a href="/" class="router-link-active router-link-active router-link-exact-active navbar-brand" aria-current="page">
                <img src="/images/logo-celnet.png" alt="logo" class="img-fluid" style="height: 2.5rem;">
            </a>

            <button data-v-5528e624="" class="navbar-toggler" type="button" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation"><span data-v-5528e624="" class="navbar-toggler-icon"></span></button>

            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item">
                        <a href="/" class="router-link-active router-link-exact-active nav-link" aria-current="page">Inicio</a>
                    </li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" role="button" aria-expanded="false"> Procesos </a>
                        <ul class="dropdown-menu">
                            <li>
                               <a class="dropdown-item">Cargar Excel</a> 
                            </li>
                            <li>
                                <a class="dropdown-item">Recepcion Contador</a>
                            </li>
                        </ul>
                    </li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" role="button" aria-expanded="false"> Estado Contadores </a>
                        <ul class="dropdown-menu">
                            <li>
                                <a class="dropdown-item">Recuperados</a>
                            </li>
                            <li>
                               <a class="dropdown-item">Chatarra</a> 
                            </li>
                        </ul>
                    </li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" role="button" aria-expanded="false"> Tablas </a>
                        <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
                            <li>
                                <a class="dropdown-item">Almacen</a>
                            </li>
                            <li>
                                <a class="dropdown-item">Diagnostico</a>
                            </li>
                            <li>
                                <a class="dropdown-item">Distribuidora</a>
                            </li>
                            <li>
                                <a class="dropdown-item">Fabricante</a>
                            </li>
                            <li>
                                <a class="dropdown-item">Modelo</a>
                            </li>
                            <li>
                                <a class="dropdown-item">Fichero</a>
                            </li>
                            <li>
                                <a class="dropdown-item">Proceso</a>
                            </li>
                            <li>
                                <a class="dropdown-item">General</a>
                            </li>
                            <li class="nav-item">
                                <a href="/lotes" class="nav-link">Lotes</a>
                            </li>
                            <li class="nav-item">
                                <a href="/asignar" class="nav-link">Asignar lote</a>
                            </li>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>
    </nav>
</template>


<style scoped>

.navbar {
    width: 100%;
    background-color: #274469;
    margin-top: 0px !important;
    padding-top: 0px !important;
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    justify-content: space-between;
    padding: var(--bs-navbar-padding-y) var(--bs-navbar-padding-x);
}

.container {
    display: flex;
    flex-wrap: inherit;
    align-items: center;
    justify-content: space-between;
    width: 100% !important;
    max-width: 100%;
    display: flex;
    align-items: center;
    justify-content: space-between;
}

.navbar-brand {
    padding-top: 0.3125rem;
    padding-bottom: 0.3125rem;
    margin-right: 1rem;
    font-size: 1.25rem;
    color: rgba(0,0,0,1);
    text-decoration: none;
    white-space: nowrap;
}

.img-fluid, .img-thumbnail {
    max-width: 100%;
}

img, svg {
    vertical-align: middle;
}





@media (min-width: 992px) {
    .navbar-expand-lg {
        flex-wrap: nowrap;
        justify-content: flex-start;
    }
}

</style>