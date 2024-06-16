<template>
  <div id='calendar'></div>
</template>

<script setup>

import {ref, onMounted, watch, inject} from 'vue';
import {Calendar} from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';

const emit = defineEmits();
const props = defineProps({
  reservations: Object
});

var selectedMonth = ref(null);
var selectedDay = ref(null);

// 캘린더 날짜 선택 시 스타일 적용
const dateClick = async function (info) {
  if (selectedDay.value) {
    selectedDay.value.style.borderColor = '';
    selectedDay.value.style.borderWidth = '';
  }
  // 선택 시 css
  info.dayEl.style.borderColor = 'skyblue';
  info.dayEl.style.borderWidth = '5px';
  selectedDay.value = info.dayEl;

  // 선택 시 메소드 호출
  const formattedDate = formatDate(new Date(info.dateStr));

  // console.log("변환된 날짜");
  // console.log(formattedDate)

  emit('date-clicked', formattedDate);
};

// 예약 건 수 계산을 위한 변수
let response = inject('response');

// 부모 컴포넌트의 메소드 호출 감지 변수
let change = inject('change');

// 예약 건수 계산
watch(response, (newResponse) => {
  if (newResponse) {
    const events = newResponse.data.data.content ? Object.entries(newResponse.data.data.content).map(([date, reservations]) => ({
      title: `예약 ${reservations.length} 건`,
      start: date
    })) : [];

    const eventSource = calendar.getEventSourceById('reservations');
    if (eventSource) {
      eventSource.remove();
    }
    calendar.addEventSource({events, id: 'reservations'});
  }
});

let calendar = null;

// 캘린더 이벤트 업데이트
const updateEvents = () => {
  console.log("updateEvents 실행됨")
  const events = props.reservations ? Object.entries(props.reservations).map(([date, reservations]) => ({
    title: `체크인 ${reservations.length} 건`,
    start: date
  })) : [];

  // 기존의 모든 이벤트를 제거합니다.
  const currentEvents = calendar.getEvents();
  if (currentEvents) {
    currentEvents.forEach(event => event.remove());
  }

  console.log("addEventSource 실행됨")
  // 새로운 이벤트를 추가합니다.
  events.forEach(event => calendar.addEvent(event));

  change.value = false;
};

// 페이지 열리면 캘린더 렌더링
const renderCalendar = () => {
  const calendarEl = document.getElementById('calendar');
  calendar = new Calendar(calendarEl, {
    plugins: [dayGridPlugin, interactionPlugin],
    initialView: 'dayGridMonth',
    selectable: true,
    events: [],
    dateClick: dateClick,
    aspectRatio: 3.5,
    datesSet: function (info) {

      let firstWeekStartDate = new Date(info.startStr);
      firstWeekStartDate.setDate(firstWeekStartDate.getDate() + 7);
      let year = firstWeekStartDate.getFullYear();
      let month = String(firstWeekStartDate.getMonth() + 1).padStart(2, '0'); // Months are 0-based, so we add 1
      let day = String(firstWeekStartDate.getDate()).padStart(2, '0');
      selectedMonth.value = `${year}-${month}-${day}T00:00:00`;
      emit('month-changed', selectedMonth.value);
    }
  });
  calendar.render();
  updateEvents();
};

// 부모 컴포넌트(ReservationList.vue) 백엔드 메소드 호출 감지
watch(change, (newValue) => {
  if (newValue) {
    if (!calendar) {
      renderCalendar();
    } else {
      updateEvents();
    }
  }
});

// 페이지 접속 시점에 캘린더 렌더링
onMounted(renderCalendar);

// 날짜 형식 변환(FullCalendar의 date형식 -> yyyy-MM-dd)
function formatDate(date) {
  if (date == null) {
    date = new Date();
  }

  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0'); // Months are 0-based
  const day = String(date.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
}

</script>