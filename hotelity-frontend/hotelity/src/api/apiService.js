import apiClient from './apiClient';

/* 로그인 */
export const login = async (loginInfo) => {
    return await apiClient.post('/login', loginInfo);
}

export const logout = async () => {
    return await apiClient.post('/logout');
}

/* 토큰 재발급 */
export const reissue = async () => {
    return await apiClient.post('/reissue');
}



/* 대시보드 */
/* 오늘 예약 분석 */
export const getTodayReservationAnalysis = async () => {
    try {
        const response = await apiClient.get('/chat/reservations/daily');
        return response.data;
    } catch (e) {
        console.error('Error fetching today reservation analysis:', e);
        throw e;
    }
}

/* 오늘 결제 분석 */
export const getTodayPaymentAnalysis = async () => {
    try {
        const response = await apiClient.get('/chat/payments/daily');
        return response.data;
    } catch (e) {
        console.error('Error fetching today payment analysis:', e);
        throw e;
    }
}

/* 이번달, 지난달, 작년의 이번달의 예약 */
export const getMonthlyReservationsAnalysis = async () => {
    try {
        const response = await apiClient.get('/chat/reservations/monthly');
        return response.data;
    } catch (e) {
        console.error('Error fetching monthly analysis:', e);
        throw e;
    }
}

/* 이번달, 지난달, 작년의 이번달의 결제 */
export const getMonthlyPaymentsAnalysis = async () => {
    try {
        const response = await apiClient.get('/chat/payments/monthly');
        return response.data;
    } catch (e) {
        console.error('Error fetching monthly payments analysis:', e);
        throw e;
    }
}

/* 올해, 지난해의 예약 */
export const getYearlyReservationsAnalysis = async () => {
    try {
        const response = await apiClient.get('/chat/reservations/yearly');
        return response.data;
    } catch (e) {
        console.error('Error fetching yearly reservations analysis:', e);
        throw e;
    }
}

/* 올해, 지난해의 결제 */
export const getYearlyPaymentsAnalysis = async () => {
    try {
        const response = await apiClient.get('/chat/payments/yearly');
        return response.data;
    } catch (e) {
        console.error('Error fetching yearly payments analysis:', e);
        throw e;
    }
}

/* 오늘 공지 */
export const getTodayNotice = async () => {
    try {
        const response = await apiClient.get('/chat/notices/daily');
        return response.data;
    } catch (e) {
        console.error('Error fetching today notice:', e);
        throw e;
    }
}

/* 오늘 VOC */
export const getTodayVoc = async () => {
    try {
        const response = await apiClient.get('/chat/vocs/daily');
        return response.data;
    } catch (e) {
        console.error('Error fetching today voc:', e);
        throw e;
    }
}

/* 사용자 가이드 다운로드 */
export const downloadTutorial = async () => {
    try {
        const response = await apiClient.get('/file/download/tutorial.pdf', {
            responseType: 'blob', // Important
        });
        return response;
    } catch (error) {
        console.error('Error downloading file:', error);
        throw error;
    }
};

/* 고객 */
/* 고객 리스트 */
export const getCustomers = async (params) => {
    try {
        const response = await apiClient.get('/customers/page', {params});
        return response.data;
    } catch (e) {
        console.error('Error fetching customers:', e);
        throw e;
    }
}

/* 고객 상세 */
export const getCustomer = async (customerId) => {
    try {
        const response = await apiClient.get(`/customers/${customerId}/customer`);
        return response.data;
    } catch (e) {
        console.error('Error fetching customer:', e);
        throw e;
    }
}

/* 고객 등록 */
export const createCustomer = async (customerInfo) => {
    try {
        const response = await apiClient.post('/customers', customerInfo);
        return response.data;
    } catch (e) {
        console.error('Error creating customer:', e);
        throw e;
    }
}

/* 고객 수정 */
export const updateCustomer = async (customerId, customerInfo) => {
    try {
        const response = await apiClient.put(`/customers/${customerId}`, customerInfo);
        return response.data;
    } catch (e) {
        console.error('Error updating customer:', e);
        throw e;
    }
}

/* 고객 삭제 */
export const deleteCustomer = async (customerId) => {
    try {
        const response = await apiClient.delete(`/customers/${customerId}`);
        return response.data;
    } catch (e) {
        console.error('Error deleting customer:', e);
        throw e;
    }
}

/* 고객 엑셀 업로드 */
export const uploadExcel = async (formData) => {
    try {
        const response = await apiClient.post('/customers/excel/read', formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        });
        return response.data;
    } catch (e) {
        console.error('Error uploading excel:', e);
        throw e;
    }
}

/* 고객 엑셀 다운로드 */
export const downloadExcel = async (params) => {
    try {
        const response = await apiClient.get('/customers/excel/download', {
            params: params,
            responseType: 'blob'
        });
        return response.data;
    } catch (e) {
        console.error('Error downloading excel:', e);
        throw e;
    }
}

/* 국가 조회 */
export const getNations = async () => {
    try {
        const response = await apiClient.get('/nations');
        return response.data;
    } catch (e) {
        console.error('Error fetching nations:', e);
        throw e;
    }
}



/* 직원 */
/* 직원 리스트 */
export const getEmployees = async (params) => {
    try {
        return await apiClient.get('/employees/page', {params});
    } catch (e) {
        console.error('Error fetching employees:', e);
        throw e;
    }
}

/* 직원 상세 */
export const getEmployee = async (employeeId) => {
    try {
        const response = await apiClient.get(`/employees/${employeeId}`);
        return response.data;
    } catch (e) {
        console.error('Error fetching employee:', e);
        throw e;
    }
}

/* 직원 등록 */
export const createEmployee = async (employeeInfo) => {
    try {
        const response = await apiClient.post('/employees', employeeInfo);
        return response.data;
    } catch (e) {
        console.error('Error creating employee:', e);
        throw e;
    }
}

/* 직원 수정 */
export const updateEmployee = async (employeeId, employeeInfo) => {
    try {
        const response = await apiClient.put(`/employees/${employeeId}`, employeeInfo);
        return response.data;
    } catch (e) {
        console.error('Error updating employee:', e);
        throw e;
    }
}

/* 직원 삭제 */
export const deleteEmployee = async (employeeId) => {
    try {
        const response = await apiClient.delete(`/employees/${employeeId}`);
        return response.data;
    } catch (e) {
        console.error('Error deleting employee:', e);
        throw e;
    }
}

/* 직원 엑셀 디운로드 */
export const downloadEmployeeExcel = async (params) => {
    try {
        const response = await apiClient.get('/employees/excel', {
            params: params,
            responseType: 'blob'
        });
        return response.data;
    } catch (e) {
        console.error('Error downloading employee excel:', e);
        throw e;
    }
}

/* 직원 이미지 업로드 */
export const uploadEmployeeImage = async (employeeId, formData) => {
    try {
        return await apiClient.post(`/employees/${employeeId}/image`, formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        });
    } catch (e) {
        console.error('Error uploading employee image:', e);
        throw e;
    }
}

/* 직원 이미지 삭제 */
export const deleteEmployeeImage = async (employeeId) => {
    try {
        const response = await apiClient.delete(`/employees/${employeeId}/image`);
        return response.data;
    } catch (e) {
        console.error('Error deleting employee image:', e);
        throw e;
    }
}



/* 호텔관리 */
/* 부대시설 리스트 */
export const getFacilities = async (params) => {
    try {
        return await apiClient.get('/hotel-management/facilities', {params});
    } catch (e) {
        console.error('Error fetching facilities:', e);
        throw e;
    }
}

/* 부대시설 상세 */

export const getAncillary = async (AncillaryId) => {
    try {
        const response = await apiClient.get(`/hotel-management/facilities/${AncillaryId}`);
        return response.data;
    } catch (e) {
        console.error('Error fetching ancillary:', e);
        throw e;
    }
}

/* 부대시설 등록 */
export const createFacility = async (facilityInfo) => {
    try {
        const response = await apiClient.post('/hotel-management/facilities', facilityInfo);
        return response.data;
    } catch (e) {
        console.error('Error creating facility:', e);
        throw e;
    }
}

/* 부대시설 수정 */
export const updateFacility = async (facilityId, facilityInfo) => {
    try {
        const response = await apiClient.put(`/hotel-management/facilities/${facilityId}`, facilityInfo);
        return response.data;
    } catch (e) {
        console.error('Error updating facility:', e);
        throw e;
    }
}

/* 부대시설 삭제 */
export const deleteFacility = async (facilityId) => {
    try {
        const response = await apiClient.delete(`/hotel-management/facilities/${facilityId}`);
        return response.data;
    } catch (e) {
        console.error('Error deleting facility:', e);
        throw e;
    }
}

/* 부대시설 엑셀 다운로드 */
export const downloadFacilityExcel = async (params) => {
    try {
        const response = await apiClient.get('/hotel-management/facilities/excel/download', {
            params: params,
            responseType: 'blob'
        });
        return response.data;
    } catch (e) {
        console.error('Error downloading facility excel:', e);
        throw e;
    }
}

/* 지점 리스트 */
export const getBranches = async (params) => {
    try {
        const response = await apiClient.get('/hotel-management/branches', {params});
        return response.data;
    } catch (e) {
        console.error('Error fetching branches:', e);
        throw e;
    }
}

/* 지점 등록 */
export const createBranch = async (branchInfo) => {
    try {
        const response = await apiClient.post('/hotel-management/branches', branchInfo);
        return response.data;
    } catch (e) {
        console.error('Error creating branch:', e);
        throw e;
    }
}

/* 지점 수정 */
export const updateBranch = async (branchId, branchInfo) => {
    try {
        const response = await apiClient.put(`/hotel-management/branches/${branchId}`, branchInfo);
        return response.data;
    } catch (e) {
        console.error('Error updating branch:', e);
        throw e;
    }
}

/* 지점 삭제 */
export const deleteBranch = async (branchId) => {
    try {
        const response = await apiClient.delete(`/hotel-management/branches/${branchId}`);
        return response.data;
    } catch (e) {
        console.error('Error deleting branch:', e);
        throw e;
    }
}

/* 객실 리스트 */
export const getRooms = async (params) => {
    try {
        return await apiClient.get('/hotel-management/rooms', {params});
    } catch (e) {
        console.error('Error fetching rooms:', e);
        throw e;
    }
}

/* 객실 상세 */
export const getRoom = async (roomId) => {
    try {
        const response = await apiClient.get(`/hotel-management/rooms/${roomId}`);
        return response.data;
    } catch (e) {
        console.error('Error fetching room:', e);
        throw e;
    }
}

/* 객실 수정 */
export const updateRoom = async (roomId, roomInfo) => {
    try {
        const response = await apiClient.put(`/hotel-management/rooms/${roomId}`, roomInfo);
        return response.data;
    } catch (e) {
        console.error('Error updating room:', e);
        throw e;
    }
}

/* 객실 삭제 */
export const deleteRoom = async (roomId) => {
    try {
        const response = await apiClient.delete(`/hotel-management/rooms/${roomId}`);
        return response.data;
    } catch (e) {
        console.error('Error deleting room:', e);
        throw e;
    }
}

/* 객실 엑셀 다운로드 */
export const downloadRoomExcel = async (params) => {
    try {
        const response = await apiClient.get('/hotel-management/rooms/excel/download', {
            params: params,
            responseType: 'blob'
        });
        return response.data;
    } catch (e) {
        console.error('Error downloading room excel:', e);
        throw e;
    }
}



/* 호텔서비스 */
/* 결제 내역 리스트 */
export const getPayments = async (params) => {
    try {
        return await apiClient.get('/hotel-service/payments/page', {params});
    } catch (e) {
        console.error('Error fetching payments:', e);
        throw e;
    }
}

/* 결제 엑셀 다운로드 */
export const downloadPaymentExcel = async (params) => {
    try {
        const response = await apiClient.get('/hotel-service/payments/excel/download', {
            params: params,
            responseType: 'blob'
        });
        return response.data;
    } catch (e) {
        console.error('Error downloading payment excel:', e);
        throw e;
    }
}

/* 월별 예약 리스트 */
export const getMonthlyReservations = async (checkinDate, params) => {
    try {
        const response = await apiClient.get(`/hotel-service/reservations/${checkinDate}`, {params});
        return response.data;
    } catch (e) {
        console.error('Error fetching monthly reservations:', e);
        throw e;
    }
}

/* 최신 순 예약 리스트 */
export const getRecentReservations = async () => {
    try {
        const response = await apiClient.get('/hotel-service/reservations/latest');
        return response.data;
    } catch (e) {
        console.error('Error fetching recent reservations:', e);
        throw e;
    }
}

/* 연도별 예약 리스트 */
export const getYearlyReservations = async (year) => {
    try {
        const response = await apiClient.get(`/hotel-service/reservations/year/${year}`);
        return response.data;
    } catch (e) {
        console.error('Error fetching yearly reservations:', e);
        throw e;
    }
}

/* 예약 상세 */
export const getReservation = async (reservationId) => {
    try {
        const response = await apiClient.get(`/hotel-service/reservations/${reservationId}/selected`);
        return response.data;
    } catch (e) {
        console.error('Error fetching reservation:', e);
        throw e;
    }
}

/* 일자별 예약 리스트 */
export const getDailyReservations = async (today) => {
    try {
        const response = await apiClient.get(`/hotel-service/reservations/${today}/day`);
        return response.data;
    } catch (e) {
        console.error('Error fetching daily reservations:', e);
        throw e;
    }
}

/* 예약 엑셀 다운로드 */
export const downloadReservationExcel = async (checkinDate, params) => {
    try {
        const response = await apiClient.get(`/hotel-service/reservations/${checkinDate}/excel/download`, {
            params: params,
            responseType: 'blob'
        });
        return response.data;
    } catch (e) {
        console.error('Error downloading reservation excel:', e);
        throw e;
    }
}

/* 투숙 리스트 */
export const getStays = async (params) => {
    try {
        const response = await apiClient.get('/hotel-service/stays/page', {params});
        return response.data;
    } catch (e) {
        console.error('Error fetching stays:', e);
        throw e;
    }
}

/* 일자별 투숙 내역 */
export const getDailyStays = async (date) => {
    try {
        const response = await apiClient.get(`/hotel-service/stays/daily/${date}`);
        return response.data;
    } catch (e) {
        console.error('Error fetching daily stays:', e);
        throw e;
    }
}

/* 투숙 상세 */
export const getStay = async (stayId) => {
    try {
        const response = await apiClient.get(`/hotel-service/stays/${stayId}/selected`);
        return response.data;
    } catch (e) {
        console.error('Error fetching stay:', e);
        throw e;
    }
}

/* 투숙 생성 */
export const createStay = async (stayInfo) => {
    try {
        const response = await apiClient.post('/hotel-service/stays/checkin', stayInfo);
        return response.data;
    } catch (e) {
        console.error('Error creating stay:', e);
        throw e;
    }
}

/* 체크아웃 */
export const checkout = async (stayId) => {
    try {
        const response = await apiClient.put(`/hotel-service/stays/${stayId}/checkout`);
        return response.data;
    } catch (e) {
        console.error('Error checking out:', e);
        throw e;
    }
}

/* 투숙 수정 */
export const updateStay = async (stayId, stayInfo) => {
    try {
        const response = await apiClient.put(`/hotel-service/stays/${stayId}`, stayInfo);
        console.log(response.data)
        return response.data;
    } catch (e) {
        console.error('Error updating stay:', e);
        throw e;
    }
}

/* 투숙 삭제 */
export const deleteStay = async (stayId) => {
    try {
        const response = await apiClient.delete(`/hotel-service/stays/${stayId}`);
        return response.data;
    } catch (e) {
        console.error('Error deleting stay:', e);
        throw e;
    }
}

/* 투숙 엑셀 다운로드 */
export const downloadStayExcel = async (params) => {
    try {
        const response = await apiClient.get('/hotel-service/stays/page/excel/download', {
            params: params,
            responseType: 'blob'
        });
        return response.data;
    } catch (e) {
        console.error('Error downloading stay excel:', e);
        throw e;
    }
}



/* 마케팅 */
/* 캠페인 발송 */
export const sendCampaign = async (campaignInfo) => {
    try {
        return await apiClient.post('/mail/level', campaignInfo);
    } catch (e) {
        console.error('Error sending campaign:', e);
        throw e;
    }
}

/* 캠페인 발송 리스트 */
export const getCampaigns = async (params) => {
    try {
        return await apiClient.get('/marketing/campaigns/search/page', {params});
    } catch (e) {
        console.error('Error fetching campaigns:', e);
        throw e;
    }
}

/* 캠페인 상세 */
export const getCampaign = async (campaignId) => {
    try {
        const response = await apiClient.get(`/marketing/campaigns/${campaignId}/campaign`);
        return response.data;
    } catch (e) {
        console.error('Error fetching campaign:', e);
        throw e;
    }
}

/* 템플릿 리스트 */
export const getTemplatePage = async (params) => {
    try {
        const response = await apiClient.get('/marketing/templates/page', {params});
        return response.data;
    } catch (e) {
        console.error('Error fetching templates:', e);
        throw e;
    }
}

export const getTemplates = async () => {
    try {
        const response = await apiClient.get('/marketing/templates');
        return response.data;
    } catch (e) {
        console.error('Error fetching templates:', e);
        throw e;
    }
}

/* 템플릿 상세 */
export const getTemplate = async (templateId) => {
    try {
        const response = await apiClient.get(`/marketing/templates/${templateId}/template`);
        return response.data;
    } catch (e) {
        console.error('Error fetching template:', e);
        throw e;
    }
}

/* 템플릿 생성 */
export const createTemplate = async (templateInfo) => {
    try {
        const response = await apiClient.post('/marketing/templates', templateInfo);
        return response.data;
    } catch (e) {
        console.error('Error creating template:', e);
        throw e;
    }
}

/* 영업관리 */
/* 쿠폰 리스트 */
export const getCoupons = async (params) => {
    try {
        return await apiClient.get('/sales/coupons/page', {params});
    } catch (e) {
        console.error('Error fetching coupons:', e);
        throw e;
    }
}

/* 쿠폰 생성 */
export const createCoupon = async (couponInfo) => {
    try {
        const response = await apiClient.post('/sales/coupons', couponInfo);
        return response.data;
    } catch (e) {
        console.error('Error creating coupon:', e);
        throw e;
    }
}

/* 쿠폰 수정 */
export const updateCoupon = async (couponId, couponInfo) => {
    try {
        const response = await apiClient.put(`/sales/${couponId}/coupons`, couponInfo);
        return response.data;
    } catch (e) {
        console.error('Error updating coupon:', e);
        throw e;
    }
}

/* 쿠폰 엑셀 다운로드 */
export const downloadCouponExcel = async (params) => {
    try {
        const response = await apiClient.get('/sales/coupons/page/excel/download', {
            params: params,
            responseType: 'blob'
        });
        return response.data;
    } catch (e) {
        console.error('Error downloading coupon excel:', e);
        throw e;
    }
}

/* 쿠폰 발급 리스트 */
export const getCouponIssues = async (params) => {
    try {
        const response = await apiClient.get('/sales/coupons/issue/page', {params});
        return response.data;
    } catch (e) {
        console.error('Error fetching coupon issues:', e);
        throw e;
    }
}

/* 쿠폰 발급 */
export const issueCoupon = async (couponIssueInfo) => {
    try {
        const response = await apiClient.post('/sales/coupons/issue', couponIssueInfo);
        return response.data;
    } catch (e) {
        console.error('Error issuing coupon:', e);
        throw e;
    }
}

/* 발급 쿠폰 엑셀 다운로드 */
export const downloadCouponIssueExcel = async (params) => {
    try {
        const response = await apiClient.get('/sales/coupons/issue/excel/download', {
            params: params,
            responseType: 'blob'
        });
        return response.data;
    } catch (e) {
        console.error('Error downloading coupon issue excel:', e);
        throw e;
    }
}

/* 멤버십 리스트 */
export const getMemberships = async (params) => {
    try {
        const response = await apiClient.get('/sales/membership', {params});
        return response.data;
    } catch (e) {
        console.error('Error fetching memberships:', e);
        throw e;
    }
}

/* 공지사항 리스트 */
export const getNotices = async (params) => {
    try {
        return await apiClient.get('/sales/notices/page', {params});
    } catch (e) {
        console.error('Error fetching notices:', e);
        throw e;
    }
}

/* 최신순 공지 리스트 */
export const getRecentNotices = async () => {
    try {
        const response = await apiClient.get('/sales/notices/latest');
        return response.data;
    } catch (e) {
        console.error('Error fetching recent notices:', e);
        throw e;
    }
}

/* 공지 상세 */
export const getNotice = async (noticeId) => {
    try {
        const response = await apiClient.get(`/sales/notices/${noticeId}/notice`);
        return response.data;
    } catch (e) {
        console.error('Error fetching notice:', e);
        throw e;
    }
}

/* 공지 등록 */
export const createNotice = async (noticeInfo) => {
    try {
        const response = await apiClient.post('/sales/notices', noticeInfo);
        return response.data;
    } catch (e) {
        console.error('Error creating notice:', e);
        throw e;
    }
}

/* 공지 수정 */
export const updateNotice = async (noticeId, noticeInfo) => {
    try {
        const response = await apiClient.put(`/sales/notices/${noticeId}`, noticeInfo);
        return response.data;
    } catch (e) {
        console.error('Error updating notice:', e);
        throw e;
    }
}

/* 공지 삭제 */
export const deleteNotice = async (noticeId) => {
    try {
        const response = await apiClient.delete(`/sales/notices/${noticeId}`);
        return response.data;
    } catch (e) {
        console.error('Error deleting notice:', e);
        throw e;
    }
}



/* VOC 리스트 */
export const getVocs = async (params) => {
    try {
        return await apiClient.get('/sales/vocs/page', {params});
    } catch (e) {
        console.error('Error fetching vocs:', e);
        throw e;
    }
}

/* 최신순 VOC 리스트 */
export const getRecentVocs = async () => {
    try {
        const response = await apiClient.get('/sales/vocs/latest');
        return response.data;
    } catch (e) {
        console.error('Error fetching recent vocs:', e);
        throw e;
    }
}

/* VOC 상세 */
export const getVoc = async (vocId) => {
    try {
        const response = await apiClient.get(`/sales/vocs/${vocId}/voc`);
        return response.data;
    } catch (e) {
        console.error('Error fetching voc:', e);
        throw e;
    }
}

/* VOC 수정 */
export const replyVoc = async (vocId, vocInfo) => {
    try {
        console.log('vocInfo:', vocInfo);
        const response = await apiClient.put(`/sales/vocs/${vocId}`, vocInfo);
        return response.data;
    } catch (e) {
        console.error('Error updating employee:', e);
        throw e;
    }
}

/* VOC 식제 */
export const deleteVoc = async (vocId) => {
    try {
        const response = await apiClient.delete(`/sales/vocs/${vocId}`);
        return response.data;
    } catch (e) {
        console.error('Error deleting voc:', e);
        throw e;
    }
}

/* VOC 댓글 등록 */
export const createVocComment = async (vocId, commentInfo) => {
    try {
        const response = await apiClient.post(`/sales/vocs/${vocId}/comments`, commentInfo);
        return response.data;
    } catch (e) {
        console.error('Error creating voc comment:', e);
        throw e;
    }
}

/* VOC 엑셀 다운로드 */
export const downloadVocExcel = async (params) => {
    try {
        const response = await apiClient.get('/sales/vocs/excel/download', {
            params: params,
            responseType: 'blob'
        });
        return response.data;
    } catch (e) {
        console.error('Error downloading voc excel:', e);
        throw e;
    }
}
