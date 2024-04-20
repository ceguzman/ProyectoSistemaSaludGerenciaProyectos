import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { TypeSpecialistService } from '../service/type-specialist.service';
import { ITypeSpecialist } from '../type-specialist.model';
import { TypeSpecialistFormService } from './type-specialist-form.service';

import { TypeSpecialistUpdateComponent } from './type-specialist-update.component';

describe('TypeSpecialist Management Update Component', () => {
  let comp: TypeSpecialistUpdateComponent;
  let fixture: ComponentFixture<TypeSpecialistUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let typeSpecialistFormService: TypeSpecialistFormService;
  let typeSpecialistService: TypeSpecialistService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, TypeSpecialistUpdateComponent],
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
      .overrideTemplate(TypeSpecialistUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TypeSpecialistUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    typeSpecialistFormService = TestBed.inject(TypeSpecialistFormService);
    typeSpecialistService = TestBed.inject(TypeSpecialistService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const typeSpecialist: ITypeSpecialist = { id: 456 };

      activatedRoute.data = of({ typeSpecialist });
      comp.ngOnInit();

      expect(comp.typeSpecialist).toEqual(typeSpecialist);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeSpecialist>>();
      const typeSpecialist = { id: 123 };
      jest.spyOn(typeSpecialistFormService, 'getTypeSpecialist').mockReturnValue(typeSpecialist);
      jest.spyOn(typeSpecialistService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeSpecialist });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typeSpecialist }));
      saveSubject.complete();

      // THEN
      expect(typeSpecialistFormService.getTypeSpecialist).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(typeSpecialistService.update).toHaveBeenCalledWith(expect.objectContaining(typeSpecialist));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeSpecialist>>();
      const typeSpecialist = { id: 123 };
      jest.spyOn(typeSpecialistFormService, 'getTypeSpecialist').mockReturnValue({ id: null });
      jest.spyOn(typeSpecialistService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeSpecialist: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typeSpecialist }));
      saveSubject.complete();

      // THEN
      expect(typeSpecialistFormService.getTypeSpecialist).toHaveBeenCalled();
      expect(typeSpecialistService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeSpecialist>>();
      const typeSpecialist = { id: 123 };
      jest.spyOn(typeSpecialistService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeSpecialist });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(typeSpecialistService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
