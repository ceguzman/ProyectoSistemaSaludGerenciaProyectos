import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { ITypeSpecialist } from 'app/entities/type-specialist/type-specialist.model';
import { TypeSpecialistService } from 'app/entities/type-specialist/service/type-specialist.service';
import { MedicalAppointmentsService } from '../service/medical-appointments.service';
import { IMedicalAppointments } from '../medical-appointments.model';
import { MedicalAppointmentsFormService } from './medical-appointments-form.service';

import { MedicalAppointmentsUpdateComponent } from './medical-appointments-update.component';

describe('MedicalAppointments Management Update Component', () => {
  let comp: MedicalAppointmentsUpdateComponent;
  let fixture: ComponentFixture<MedicalAppointmentsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let medicalAppointmentsFormService: MedicalAppointmentsFormService;
  let medicalAppointmentsService: MedicalAppointmentsService;
  let typeSpecialistService: TypeSpecialistService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, MedicalAppointmentsUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(MedicalAppointmentsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MedicalAppointmentsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    medicalAppointmentsFormService = TestBed.inject(MedicalAppointmentsFormService);
    medicalAppointmentsService = TestBed.inject(MedicalAppointmentsService);
    typeSpecialistService = TestBed.inject(TypeSpecialistService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TypeSpecialist query and add missing value', () => {
      const medicalAppointments: IMedicalAppointments = { id: 456 };
      const typeSpecialist: ITypeSpecialist = { id: 27245 };
      medicalAppointments.typeSpecialist = typeSpecialist;

      const typeSpecialistCollection: ITypeSpecialist[] = [{ id: 20653 }];
      jest.spyOn(typeSpecialistService, 'query').mockReturnValue(of(new HttpResponse({ body: typeSpecialistCollection })));
      const additionalTypeSpecialists = [typeSpecialist];
      const expectedCollection: ITypeSpecialist[] = [...additionalTypeSpecialists, ...typeSpecialistCollection];
      jest.spyOn(typeSpecialistService, 'addTypeSpecialistToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ medicalAppointments });
      comp.ngOnInit();

      expect(typeSpecialistService.query).toHaveBeenCalled();
      expect(typeSpecialistService.addTypeSpecialistToCollectionIfMissing).toHaveBeenCalledWith(
        typeSpecialistCollection,
        ...additionalTypeSpecialists.map(expect.objectContaining),
      );
      expect(comp.typeSpecialistsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const medicalAppointments: IMedicalAppointments = { id: 456 };
      const typeSpecialist: ITypeSpecialist = { id: 18557 };
      medicalAppointments.typeSpecialist = typeSpecialist;

      activatedRoute.data = of({ medicalAppointments });
      comp.ngOnInit();

      expect(comp.typeSpecialistsSharedCollection).toContain(typeSpecialist);
      expect(comp.medicalAppointments).toEqual(medicalAppointments);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMedicalAppointments>>();
      const medicalAppointments = { id: 123 };
      jest.spyOn(medicalAppointmentsFormService, 'getMedicalAppointments').mockReturnValue(medicalAppointments);
      jest.spyOn(medicalAppointmentsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ medicalAppointments });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: medicalAppointments }));
      saveSubject.complete();

      // THEN
      expect(medicalAppointmentsFormService.getMedicalAppointments).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(medicalAppointmentsService.update).toHaveBeenCalledWith(expect.objectContaining(medicalAppointments));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMedicalAppointments>>();
      const medicalAppointments = { id: 123 };
      jest.spyOn(medicalAppointmentsFormService, 'getMedicalAppointments').mockReturnValue({ id: null });
      jest.spyOn(medicalAppointmentsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ medicalAppointments: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: medicalAppointments }));
      saveSubject.complete();

      // THEN
      expect(medicalAppointmentsFormService.getMedicalAppointments).toHaveBeenCalled();
      expect(medicalAppointmentsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMedicalAppointments>>();
      const medicalAppointments = { id: 123 };
      jest.spyOn(medicalAppointmentsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ medicalAppointments });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(medicalAppointmentsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTypeSpecialist', () => {
      it('Should forward to typeSpecialistService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(typeSpecialistService, 'compareTypeSpecialist');
        comp.compareTypeSpecialist(entity, entity2);
        expect(typeSpecialistService.compareTypeSpecialist).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
