/*
 * MIT License
 * Copyright (c) [2024] [Natasha Shahran]
 *
 * Permission is granted under the MIT License to use, modify, and distribute
 * this software, provided credit is given to the original creator ([Natasha Shahran]).
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 */import { createRouter, createWebHistory } from "vue-router";



// Import components
const HomeView = () => import('../components/Home.vue');
const FoodItem = () => import('../components/FoodItem.vue');
const RecipeGenerator = () => import('../components/RecipeGenerator.vue');
const SavedRecipes = () => import('../components/SavedRecipes.vue');
const LoginForm = () => import("../components/LoginForm.vue");
const SignUpForm = () => import("../components/SignUpForm.vue");
const UserProfile = () => import('../components/UserProfile.vue');
const WelcomeView = () => import('../components/WelcomeView.vue');
const VerificationSuccess = () => import('../components/VerificationSuccess.vue');
const VerificationExpired = () => import('../components/VerificationExpired.vue');
const VerificationError = () => import('../components/VerificationError.vue');
// Define routes
const routes = [
    { path: '/', redirect: '/home' }, // Redirect root path to /home
    { path: '/home', name: 'Home', component: HomeView },
    { path: '/welcome', name: 'WelcomeView', component: WelcomeView, meta: { requiresAuth: true } },
    { path: '/verification-success', name: 'VerificationSuccess', component: VerificationSuccess },
    { path: '/verification-expired', name: 'VerificationExpired', component: VerificationExpired },
    { path: '/verification-error', name: 'VerificationError', component: VerificationError},
    { path: '/ingredient-list', name: 'IngredientList', component: FoodItem },
    { path: '/recipe-generator', name: 'RecipeGenerator', component: RecipeGenerator },
    { path: '/saved-recipes', name: 'SavedRecipes', component: SavedRecipes, meta: { requiresAuth: true } },
    { path: '/login', name: 'Login', component: LoginForm },
    { path: '/signup', name: 'SignUp', component: SignUpForm },
    { path: '/profile', name: 'Profile', component: UserProfile, meta: { requiresAuth: true } },
];

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes,
});


export default router;