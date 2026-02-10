import { createRouter, createWebHistory } from 'vue-router'
import UserListView from '../views/UserListView.vue'
import TaskListView from '../views/TaskListView.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', redirect: '/users' },
    { path: '/users', component: UserListView },
    { path: '/tasks', component: TaskListView },
  ],
})

export default router
