import { createRouter, createWebHistory } from 'vue-router';
import { useAuthStore } from '../store';

import Home from '@/component/home/Home.vue';
import Login from '@/component/login/Login.vue';

import Customer from "@/component/customer/Customer.vue";
import CustomerInit from '@/component/customer/CustomerInit.vue';
import CustomerList from '@/component/customer/CustomerList.vue';

import EmployeeList from '@/component/employee/EmployeeList.vue';
import EmployeeInfo from '@/component/employee/EmployeeInfo.vue';

import BranchList from '@/component/hotel-management/BranchList.vue';
import BranchInfo from '@/component/hotel-management/BranchInfo.vue';
import RoomList from "@/component/hotel-management/RoomList.vue";
import RoomDetails from "@/component/hotel-management/RoomDetails.vue";
import Ancillary from "@/component/hotel-management/Ancillary.vue";
import AncillaryDetails from "@/component/hotel-management/AncillaryDetails.vue";

import ReservationList from "@/component/hotel-service/reservation/ReservationList.vue";
import StayList from "@/component/hotel-service/stay/StayList.vue";
import PaymentList from "@/component/hotel-service/payment/PaymentList.vue";

import Voc from "@/component/sales/Voc.vue";
import Notice from "@/component/sales/Notice.vue";
import NoticeInfo from "@/component/sales/NoticeInfo.vue";
import NoticeInit from "@/component/sales/NoticeInit.vue";
import Membership from "@/component/sales/Membership.vue";
import Coupon from "@/component/sales/Coupon.vue";
import CouponIssue from "@/component/sales/CouponIssue.vue";

import Campaign from "@/component/marketing/Campaign.vue";
import CampaignSend from "@/component/marketing/CampaignSend.vue";
import TemplateList from "@/component/marketing/TemplateList.vue";
import TemplateInfo from "@/component/marketing/TemplateInfo.vue";
import TemplateSelect from "@/component/marketing/TemplateSelect.vue";

// 테스트 용
import VocSelect from "@/component/sales/VocSelect.vue";

const routes = [
    {
        path: '/login',
        name: 'Login',
        component: Login,
        meta: {
            hideSidebar: true,
            hideNavbar: true,
            hideFooter: true
        }
    },

    /* 대시보드 */
    {
        path: '/',
        name: 'Home',
        component: Home,
        meta: {
            requiresAuth: true,
            category: 'home',
        },
    },

    /* 고객 */
    {
        path: '/customer/:id',
        name: 'Customer',
        component: Customer,
        meta: {
            requiresAuth: true,
            category: 'customer',
        },
    },
    {
        path: '/customerInit',
        name: 'CustomerInit',
        component: CustomerInit,
        meta: {
            requiresAuth: true,
            category: 'customer',
        },
    },
    {
        path: '/customerList',
        name: 'CustomerList',
        component: CustomerList,
        meta: {
            requiresAuth: true,
            category: 'customer',
        },
    },

    /* 직원 */
    {
        path: '/employeeList',
        name: 'EmployeeList',
        component: EmployeeList,
        meta: {
            requiresAuth: true,
            category: 'employee',
        },
    },
    {
        path: '/employeeInfo/:id',
        name: 'EmployeeInfo',
        component: EmployeeInfo,
        meta: {
            requiresAuth: true,
            category: 'employee',
        },
    },

    /* 호텔관리 */
    {
        path: '/branchList',
        name: 'BranchList',
        component: BranchList,
        meta: {
            requiresAuth: true,
            category: 'hotelManagement',
        },
    },
    {
        path: '/branchInfo',
        name: 'BranchInfo',
        component: BranchInfo,
        meta: {
            requiresAuth: true,
            category: 'hotelManagement',
        },
    },
    {
        path: '/roomList',
        name: 'RoomList',
        component: RoomList,
        meta: {
            requiresAuth: true,
            category: 'hotelManagement',
        },
    },
    {
        path: '/roomDetails/:id',
        name: 'RoomDetails',
        component: RoomDetails,
        meta: {
            hideSidebar: true,
            hideNavbar: true,
            hideFooter: true,
            requiresAuth: true,
            category: 'hotelManagement',
        },
    },
    {
        path: '/facilityList',
        name: 'FacilityList',
        component: Ancillary,
        meta: {
            requiresAuth: true,
            category: 'hotelManagement',
        },
    },
    {
        path: '/ancillaryDetails/:id',
        name: 'ancillaryDetails',
        component: AncillaryDetails,
        meta: {
            hideSidebar: true,
            hideNavbar: true,
            hideFooter: true,
            requiresAuth: true,
            category: 'hotelManagement',
        },
    },

    /* 호텔서비스 */
    {
        path: '/reservation',
        name: 'Reservation',

        component: ReservationList,
        meta: {
            requiresAuth: true,
            category: 'hotelService',
        },
    },
    {
        path: '/stayList',
        name: 'StayList',
        component: StayList,
        meta: {
            requiresAuth: true,
            category: 'hotelService',
        },
    },
    {
        path: '/paymentList',
        name: 'PaymentList',
        component: PaymentList,
        meta: {
            requiresAuth: true,
            category: 'hotelService',
        },
    },
    {
        path: '/paymentList/:id',
        name: 'PaymentListWithId',
        component: PaymentList,
        meta: {
            requiresAuth: true,
            category: 'hotelService',
        },
    },

    /* 마케팅 */
    {
        path: '/campaign',
        name: 'Campaign',
        component: Campaign,
        meta: {
            requiresAuth: true,
            category: 'marketing',
        },
    },
    {
        path: '/campaignSend',
        name: 'CampaignSend',
        component: CampaignSend,
        meta: {
            requiresAuth: true,
            category:'marketing',
        },
    },
    {
        path: '/templateList',
        name: 'TemplateList',
        component: TemplateList,
        meta: {
            requiresAuth: true,
            category:'marketing',
        },
    },
    {
        path: '/templateInfo/:id',
        name: 'TemplateInfo',
        component: TemplateInfo,
        meta: {
            requiresAuth: true,
            category:'marketing',
        },
    },
    {
        path: '/templateSelect/:id',
        name: 'TemplateSelect',
        component: TemplateSelect,
        meta: {
            requiresAuth: true,
            category:'marketing',
        },
    },

    /* 영업관리 */
    {
        path: '/voc',
        name: 'Voc',
        component: Voc,
        meta: {
            requiresAuth: true,
            category: 'sales',
        },
    },
    {
        path: '/voc/:id',
        name: 'VocListWithId',
        component: Voc,
        meta: {
            requiresAuth: true,
            category: 'sales',
        },
    },
    {
        path: '/notice',
        name: 'Notice',
        component: Notice,
        meta: {
            requiresAuth: true,
            category: 'sales',
        },
    },
    {
        path: '/notice/noticeInit',
        name: 'NoticeInit',
        component: NoticeInit,
        meta: {
            hideFooter: true,
            hideSidebar: true,
            hideNavbar: true,
            requiresAuth: true,
            category: 'sales',
        },
    },
    {
        path: '/noticeInfo/:id',
        name: 'NoticeInfo',
        component: NoticeInfo,
        meta: {
            requiresAuth: true,
            category: 'sales',
        },
    },
    {
        path: '/membership',
        name: 'Membership',
        component: Membership,
        meta: {
            requiresAuth: true,
            category: 'sales',
        },
    },
    {
        path: '/coupon',
        name: 'Coupon',
        component: Coupon,
        meta: {
            requiresAuth: true,
            category: 'sales',
        },
    },
    {
        path: '/couponIssue',
        name: 'CouponIssue',
        component: CouponIssue,
        meta: {
            requiresAuth: true,
            category: 'sales',
        },
    },
    {
        path: '/vocSelect/:id',
        name: 'VocSelect',
        component: VocSelect,
        meta: {
            requiresAuth: true,
            category: 'sales',
        },
    }
];

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes,
});

router.beforeEach((to, from, next) => {
    const authStore = useAuthStore();

    if (to.matched.some(record => record.meta.requiresAuth)) {
        if (!authStore.isAuthenticated) {
            next({
                path: '/login',
                query: { redirect: to.fullPath },
            });
        } else {
            next();
        }
    } else {
        next();
    }
});

export default router;
