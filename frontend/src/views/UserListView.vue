<template>
  <div>
    <h2>Users</h2>

    <div>
      <h3>{{ editingUser ? 'Edit User' : 'New User' }}</h3>
      <input v-model="form.name" placeholder="Name" />
      <input v-model="form.email" placeholder="Email" />
      <button @click="saveUser">{{ editingUser ? 'Update' : 'Create' }}</button>
      <button v-if="editingUser" @click="cancelEdit">Cancel</button>
    </div>

    <table>
      <thead>
        <tr>
          <th>ID</th>
          <th>Name</th>
          <th>Email</th>
          <th>Task Count</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="user in users" :key="user.id">
          <td>{{ user.id }}</td>
          <td>{{ user.name }}</td>
          <td>{{ user.email }}</td>
          <td>{{ user.taskCount ?? '-' }}</td>
          <td>
            <button @click="loadDetail(user.id)">Detail</button>
            <button @click="editUser(user)">Edit</button>
            <button @click="removeUser(user.id)">Delete</button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { fetchUsers, fetchUserDetail, createUser, updateUser, deleteUser } from '@/api/userApi'

const users = ref([])
const form = ref({ name: '', email: '' })
const editingUser = ref(null)

async function loadUsers() {
  users.value = await fetchUsers()
}

async function loadDetail(id) {
  const detail = await fetchUserDetail(id)
  const idx = users.value.findIndex(u => u.id === id)
  if (idx !== -1) {
    users.value[idx] = detail
  }
}

async function saveUser() {
  if (editingUser.value) {
    await updateUser(editingUser.value.id, form.value)
    editingUser.value = null
  } else {
    await createUser(form.value)
  }
  form.value = { name: '', email: '' }
  await loadUsers()
}

function editUser(user) {
  editingUser.value = user
  form.value = { name: user.name, email: user.email }
}

function cancelEdit() {
  editingUser.value = null
  form.value = { name: '', email: '' }
}

async function removeUser(id) {
  await deleteUser(id)
  await loadUsers()
}

onMounted(loadUsers)
</script>
