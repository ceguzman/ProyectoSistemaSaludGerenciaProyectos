import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'Authorities' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'people',
    data: { pageTitle: 'People' },
    loadChildren: () => import('./people/people.routes'),
  },
  {
    path: 'type-document',
    data: { pageTitle: 'TypeDocuments' },
    loadChildren: () => import('./type-document/type-document.routes'),
  },
  {
    path: 'type-specialist',
    data: { pageTitle: 'TypeSpecialists' },
    loadChildren: () => import('./type-specialist/type-specialist.routes'),
  },
  {
    path: 'medical-appointments',
    data: { pageTitle: 'MedicalAppointments' },
    loadChildren: () => import('./medical-appointments/medical-appointments.routes'),
  },
  {
    path: 'clinic-history',
    data: { pageTitle: 'ClinicHistories' },
    loadChildren: () => import('./clinic-history/clinic-history.routes'),
  },
  {
    path: 'type-diseases',
    data: { pageTitle: 'TypeDiseases' },
    loadChildren: () => import('./type-diseases/type-diseases.routes'),
  },
  {
    path: 'medical-authorization',
    data: { pageTitle: 'MedicalAuthorizations' },
    loadChildren: () => import('./medical-authorization/medical-authorization.routes'),
  },
  {
    path: 'medical-procedures',
    data: { pageTitle: 'MedicalProcedures' },
    loadChildren: () => import('./medical-procedures/medical-procedures.routes'),
  },
  {
    path: 'medication-request',
    data: { pageTitle: 'MedicationRequests' },
    loadChildren: () => import('./medication-request/medication-request.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
