<template>
  <div id="app">
    <nav>
      <router-link to="/users">Users</router-link>
      <span> | </span>
      <router-link to="/tasks">Tasks</router-link>
    </nav>
    <hr />
    <router-view />
  </div>
</template>

<style>
#app {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  font-family: sans-serif;
}
nav a {
  margin-right: 10px;
}
table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 10px;
}
th, td {
  border: 1px solid #ccc;
  padding: 8px;
  text-align: left;
}
input, select {
  padding: 4px 8px;
  margin: 4px;
}
button {
  padding: 4px 12px;
  margin: 2px;
  cursor: pointer;
}
</style>
