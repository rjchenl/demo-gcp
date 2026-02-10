<template>
  <div>
    <h2>Tasks</h2>

    <div>
      <h3>{{ editingTask ? 'Edit Task' : 'New Task' }}</h3>
      <input v-model="form.title" placeholder="Title" />
      <input v-model="form.description" placeholder="Description" />
      <input v-model.number="form.userId" placeholder="User ID" type="number" />
      <button @click="saveTask">{{ editingTask ? 'Update' : 'Create' }}</button>
      <button v-if="editingTask" @click="cancelEdit">Cancel</button>
    </div>

    <table>
      <thead>
        <tr>
          <th>ID</th>
          <th>Title</th>
          <th>Description</th>
          <th>User</th>
          <th>Completed</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="task in tasks" :key="task.id">
          <td>{{ task.id }}</td>
          <td>{{ task.title }}</td>
          <td>{{ task.description }}</td>
          <td>{{ task.userName ?? `User #${task.userId}` }}</td>
          <td>{{ task.completed ? 'Yes' : 'No' }}</td>
          <td>
            <button @click="loadDetail(task.id)">Detail</button>
            <button @click="toggleComplete(task)">{{ task.completed ? 'Undo' : 'Done' }}</button>
            <button @click="editTask(task)">Edit</button>
            <button @click="removeTask(task.id)">Delete</button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { fetchTasks, fetchTaskDetail, createTask, updateTask, deleteTask } from '@/api/taskApi'

const tasks = ref([])
const form = ref({ title: '', description: '', userId: null })
const editingTask = ref(null)

async function loadTasks() {
  tasks.value = await fetchTasks()
}

async function loadDetail(id) {
  const detail = await fetchTaskDetail(id)
  const idx = tasks.value.findIndex(t => t.id === id)
  if (idx !== -1) {
    tasks.value[idx] = detail
  }
}

async function saveTask() {
  if (editingTask.value) {
    await updateTask(editingTask.value.id, form.value)
    editingTask.value = null
  } else {
    await createTask(form.value)
  }
  form.value = { title: '', description: '', userId: null }
  await loadTasks()
}

function editTask(task) {
  editingTask.value = task
  form.value = { title: task.title, description: task.description, userId: task.userId }
}

function cancelEdit() {
  editingTask.value = null
  form.value = { title: '', description: '', userId: null }
}

async function toggleComplete(task) {
  await updateTask(task.id, { ...task, completed: !task.completed })
  await loadTasks()
}

async function removeTask(id) {
  await deleteTask(id)
  await loadTasks()
}

onMounted(loadTasks)
</script>
